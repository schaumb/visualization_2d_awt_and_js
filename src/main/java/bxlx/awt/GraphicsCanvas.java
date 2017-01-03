package bxlx.awt;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.ImageCaches;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.graphics.shapes.Shape;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
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
    private final Stack<Area> areas = new Stack<>();
    private static final ImageCaches<BufferedImage> imageCaches = new ImageCaches<>(str ->
    {
        try {
            return ImageIO.read(new File(str));
        } catch (IOException e) {
            return null;
        }
    });

    public GraphicsCanvas(Graphics2D graphics) {
        this.graphics = graphics;
        java.awt.Rectangle bound = graphics.getClipBounds();
        this.clips.add(new Rectangle(bound.getX(), bound.getY(), bound.getWidth(), bound.getHeight()));
        areas.push(new Area(bound));
        this.graphics.setClip(bound);

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
    public void fill(Shape shape) {
        graphics.fill(convertToArea(shape));
    }

    private Area convertToArea(Shape shape) {
        switch (shape.getType()) {
            case RECTANGLE:
                Rectangle rectangle = shape.getAsRectangle();
                return new Area(new java.awt.Rectangle(
                        (int) Math.round(rectangle.getStart().getX()),
                        (int) Math.round(rectangle.getStart().getY()),
                        (int) Math.round(rectangle.getSize().getWidth()),
                        (int) Math.round(rectangle.getSize().getHeight())));
            case POLYGON:
                Polygon polygon = shape.getAsPolygon();

                int[] xPoints = polygon.getPoints().stream()
                        .mapToInt(p -> (int) Math.round(p.getX())).toArray();
                int[] yPoints = polygon.getPoints().stream()
                        .mapToInt(p -> (int) Math.round(p.getY())).toArray();
                return new Area(new java.awt.Polygon(xPoints, yPoints, polygon.getPoints().size()));
            case ARC:
                Arc arc = shape.getAsArc();

                Point start = arc.getCenter().add(Direction.UP.getVector().multiple(arc.getRadius()))
                        .add(Direction.LEFT.getVector().multiple(arc.getRadius()));

                Size size = Point.same(arc.getRadius() * 2).asSize();

                int startAngle = (int) Math.round(arc.getFromRadian() / 2 / Math.PI * 360);

                int relativeAngle = (int) Math.round((arc.getToRadian() - arc.getFromRadian()) / 2 / Math.PI * 360);

                return new Area(new Arc2D.Double((int) Math.round(start.getX()),
                        (int) Math.round(start.getY()),
                        (int) Math.round(size.getWidth()),
                        (int) Math.round(size.getHeight()),
                        startAngle,
                        relativeAngle,
                        Arc2D.PIE));
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
    public void setFont(String name, int size, boolean italic, boolean bold) {
        graphics.setFont(new Font(name, (italic ? Font.ITALIC : Font.PLAIN) | (bold ? Font.BOLD : Font.PLAIN), size));
    }

    @Override
    public void fillText(String text, Point to) {
        graphics.drawString(text,
                (int) Math.round(to.getX()),
                (int) Math.round(to.getY()));
    }

    @Override
    public void clip(Shape shape) {

        Rectangle rectangle = shape.getBoundingRectangle().intersect(getBoundingRectangle());
        clips.push(rectangle);

        Area newArea = convertToArea(shape);
        if(newArea == null) return;

        newArea.intersect(new Area(graphics.getClip()));

        areas.push(newArea);
        graphics.setClip(newArea);
    }

    @Override
    public void clipInverse(Shape shape) {
        Rectangle rectangle = shape.getBoundingRectangle().intersect(getBoundingRectangle());
        clips.push(rectangle);

        Area newArea = convertToArea(shape);
        if(newArea == null) return;

        Area clippedArea = new Area(graphics.getClip());
        clippedArea.subtract(newArea);

        areas.push(clippedArea);
        graphics.setClip(clippedArea);
    }

    @Override
    public void restore() {
        clips.pop();
        areas.pop();
        graphics.setClip(areas.peek());
    }
}
