package bxlx.awt;

import bxlx.SystemSpecific;
import bxlx.graphics.ImageCaches;
import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by qqcs on 2016.12.23..
 */
public class GraphicsCanvas implements ICanvas {
    private final Graphics graphics;
    private final Stack<Rectangle> clips = new Stack<>();
    private static final ImageCaches<BufferedImage> imageCaches = new ImageCaches<>(str ->
    {
        try {
            return ImageIO.read(new File(str));
        } catch (IOException e) {
            return null;
        }
    });

    public GraphicsCanvas(Graphics graphics) {
        this.graphics = graphics;
        java.awt.Rectangle bound = graphics.getClipBounds();
        this.clips.add(new Rectangle(bound.getX(), bound.getY(), bound.getWidth(), bound.getHeight()));

        if(graphics instanceof Graphics2D) {
            ((Graphics2D) graphics).setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            ((Graphics2D) graphics).setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        }
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
    public void fillArc(Arc arc) {
        Point start = arc.getCenter().add(Direction.UP.getVector().multiple(arc.getRadius()))
                .add(Direction.LEFT.getVector().multiple(arc.getRadius()));

        Size size = Point.same(arc.getRadius() * 2).asSize();

        int startAngle = (int) Math.round(arc.getFromRadian() / 2 / Math.PI * 360);

        int relativeAngle = (int) Math.round((arc.getToRadian() - arc.getFromRadian()) / 2 / Math.PI * 360);
        graphics.fillArc((int) Math.round(start.getX()),
                (int) Math.round(start.getY()),
                (int) Math.round(size.getWidth()),
                (int) Math.round(size.getHeight()),
                startAngle,
                relativeAngle);
    }

    @Override
    public void fillRectangle(Rectangle rectangle) {
        graphics.fillRect(
                (int) Math.round(rectangle.getStart().getX()),
                (int) Math.round(rectangle.getStart().getY()),
                (int) Math.round(rectangle.getSize().getWidth()),
                (int) Math.round(rectangle.getSize().getHeight()));
    }

    @Override
    public void fillPolygon(Polygon polygon) {
        int[] xPoints = polygon.getPoints().stream()
                .mapToInt(p -> (int) Math.round(p.getX())).toArray();
        int[] yPoints = polygon.getPoints().stream()
                .mapToInt(p -> (int) Math.round(p.getY())).toArray();

        graphics.fillPolygon(xPoints, yPoints, polygon.getPoints().size());
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
    public void clip(Rectangle rectangle) {
        rectangle = rectangle.intersect(getBoundingRectangle());
        clips.push(rectangle);
        graphics.setClip(
                (int) Math.round(rectangle.getStart().getX()),
                (int) Math.round(rectangle.getStart().getY()),
                (int) Math.round(rectangle.getSize().getWidth()),
                (int) Math.round(rectangle.getSize().getHeight()));
    }

    @Override
    public void restore() {
        clips.pop();
        Rectangle rectangle = getBoundingRectangle();
        graphics.setClip(
                (int) Math.round(rectangle.getStart().getX()),
                (int) Math.round(rectangle.getStart().getY()),
                (int) Math.round(rectangle.getSize().getWidth()),
                (int) Math.round(rectangle.getSize().getHeight()));
    }
}
