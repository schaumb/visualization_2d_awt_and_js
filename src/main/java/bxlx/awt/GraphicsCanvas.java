package bxlx.awt;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.Font;
import bxlx.graphics.ICanvas;
import bxlx.graphics.ImageCaches;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.graphics.shapes.Shape;
import bxlx.system.SystemSpecific;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by qqcs on 2016.12.23..
 */
public class GraphicsCanvas implements ICanvas {
    private final Graphics2D graphics;
    private final Stack<Rectangle> clips = new Stack<>();
    private final Stack<java.awt.Shape> areas = new Stack<>();
    private final Stack<Paint> fillPaintStack = new Stack<>();
    static final ImageCaches<BufferedImage> imageCaches = new ImageCaches<>(str ->
    {
        try {
            return ImageIO.read(new File(str));
        } catch (IOException e) {
            System.err.println(e.getMessage() + " file : " + str);
            return null;
        }
    });
    private Font latestFont;

    public GraphicsCanvas(Graphics2D graphics, java.awt.Rectangle rectangle) {
        this.graphics = graphics;
        this.clips.add(new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()));
        this.graphics.setClip(rectangle);
        this.areas.push(graphics.getClip());
        this.fillPaintStack.push(graphics.getPaint());
        java.awt.Font font = graphics.getFont();
        this.latestFont = new Font(font.getFontName(), font.getSize(), font.isItalic(), font.isBold());

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return clips.peek();
    }

    @Override
    public void setColor(Color color) {
        graphics.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
    }

    @Override
    public Color getColor() {
        java.awt.Color color = graphics.getColor();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    @Override
    public void pushFillImg(String src, Size resizeImg) {
        Paint paint = new TexturePaint(imageCaches.get(src), new java.awt.Rectangle(
                new Dimension((int) resizeImg.getWidth(), (int) resizeImg.getHeight())));

        graphics.setPaint(paint);
        fillPaintStack.push(paint);
    }

    @Override
    public void popFillImg() {
        fillPaintStack.pop();
        graphics.setPaint(fillPaintStack.peek());
    }

    @Override
    public void fill(Shape shape) {
        graphics.fill(convertToShape(shape));
    }

    private java.awt.Shape convertToShape(Shape shape) {
        switch (shape.getType()) {
            case RECTANGLE:
                Rectangle rectangle = shape.getAsRectangle();
                return new java.awt.Rectangle.Double(
                        rectangle.getStart().getX(),
                        rectangle.getStart().getY(),
                        rectangle.getSize().getWidth(),
                        rectangle.getSize().getHeight());
            case POLYGON:
                Polygon polygon = shape.getAsPolygon();
                Path2D path = new Path2D.Double();

                path.moveTo(polygon.getPoints().get(0).getX(), polygon.getPoints().get(0).getY());
                for (int i = 1; i < polygon.getPoints().size(); ++i) {
                    path.lineTo(polygon.getPoints().get(i).getX(), polygon.getPoints().get(i).getY());
                }
                path.closePath();
                return path;
            case ARC:
                Arc arc = shape.getAsArc();

                Point start = arc.getCenter().add(Direction.UP.getVector().multiple(arc.getRadius()))
                        .add(Direction.LEFT.getVector().multiple(arc.getRadius()));

                Size size = Point.same(arc.getRadius() * 2).asSize();

                int startAngle = (int) Math.round(arc.getFromRadian() / 2 / Math.PI * 360);

                int relativeAngle = (int) Math.round((arc.getToRadian() - arc.getFromRadian()) / 2 / Math.PI * 360);

                return new Arc2D.Double(start.getX(),
                        start.getY(),
                        size.getWidth(),
                        size.getHeight(),
                        startAngle,
                        relativeAngle,
                        Arc2D.PIE);
        }
        return null;
    }

    @Override
    public void drawImage(String src, Rectangle to) {
        graphics.drawImage(
                imageCaches.get(src),
                (int) Math.round(to.getStart().getX()),
                (int) Math.round(to.getStart().getY()),
                (int) Math.round(to.getSize().getWidth()),
                (int) Math.round(to.getSize().getHeight()),
                null
        );
    }

    @Override
    public void setFont(Font font) {
        latestFont = font;
        graphics.setFont(new java.awt.Font(font.getName(), (font.isItalic() ? java.awt.Font.ITALIC : java.awt.Font.PLAIN) |
                (font.isBold() ? java.awt.Font.BOLD : java.awt.Font.PLAIN), font.getSize()));
    }

    @Override
    public Font getFont() {
        return latestFont;
    }

    @Override
    public void fillText(String text, Point to) {
        graphics.drawString(text,
                (int) Math.round(to.getX()),
                (int) Math.round(to.getY()));
    }

    @Override
    public int textWidth(String text) {
        return graphics.getFontMetrics().stringWidth(text);
    }

    @Override
    public void clip(Shape shape) {
        Rectangle rectangle = shape.getBoundingRectangle().intersect(getBoundingRectangle());
        clips.push(rectangle);

        java.awt.Shape newShape = convertToShape(shape);
        if (newShape == null) return;

        graphics.clip(newShape);
        areas.push(graphics.getClip());
    }

    @Override
    public void clipInverse(Shape shape) {
        Rectangle rectangle = clips.peek();
        clips.push(rectangle);

        java.awt.Shape newShape = convertToShape(shape);
        if (newShape == null) return;

        Area clippedArea = new Area(
                new java.awt.Rectangle.Double(
                        rectangle.getStart().getX(),
                        rectangle.getStart().getY(),
                        rectangle.getSize().getWidth(),
                        rectangle.getSize().getHeight()
                )
        );
        clippedArea.subtract(new Area(newShape));

        graphics.clip(clippedArea);
        areas.push(graphics.getClip());
    }

    @Override
    public void restore() {
        clips.pop();
        areas.pop();
        graphics.setClip(areas.peek());
    }

    @Override
    public void fakeClip(Rectangle rectangle) {
        clips.push(rectangle);
    }

    @Override
    public void fakeRestore() {
        clips.pop();
    }
}
