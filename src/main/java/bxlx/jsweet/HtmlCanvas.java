package bxlx.jsweet;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.ImageCaches;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.graphics.shapes.Shape;
import jsweet.dom.CanvasRenderingContext2D;
import jsweet.dom.HTMLCanvasElement;
import jsweet.dom.HTMLImageElement;
import jsweet.util.StringTypes;

import java.util.List;
import java.util.Stack;

import static jsweet.dom.Globals.document;
import static jsweet.util.Globals.union;

/**
 * Created by qqcs on 2016.12.23..
 */
public class HtmlCanvas implements ICanvas {
    private final CanvasRenderingContext2D context;
    private final Stack<Rectangle> clips = new Stack<>();
    static final ImageCaches<HTMLImageElement> imageCaches =
            new ImageCaches<>(src -> {
                HTMLImageElement img = document.createElement(StringTypes.img);
                img.src = src;
                return img;
            });
    private Color latestColor;

    public HtmlCanvas(HTMLCanvasElement canvasElement) {
        this.context = canvasElement.getContext(StringTypes._2d);
        clips.push(new Rectangle(Point.ORIGO, new Size(context.canvas.width, context.canvas.height)));
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return clips.peek();
    }

    @Override
    public void setColor(Color color) {
        if (color == null) return;
        latestColor = color;
        context.fillStyle = union(color.toString());

        context.globalAlpha = color.getAlpha();
    }

    @Override
    public Color getColor() {
        return latestColor;
    }

    @Override
    public void fill(Shape shape) {
        setShape(shape);
        context.fill();
    }

    private void setShape(Shape shape) {
        context.beginPath();
        switch (shape.getType()) {
            case ARC:
                Arc arc = shape.getAsArc();
                context.moveTo(arc.getCenter().getX(), arc.getCenter().getY());
                context.arc(arc.getCenter().getX(), arc.getCenter().getY(),
                        arc.getRadius(), Math.PI * 2 - arc.getToRadian(), Math.PI * 2 - arc.getFromRadian(),
                        arc.getFromRadian() > arc.getToRadian());
                context.lineTo(arc.getCenter().getX(), arc.getCenter().getY());
                break;
            case POLYGON:
                Polygon polygon = shape.getAsPolygon();
                List<Point> points = polygon.getPoints();
                context.moveTo(points.get(0).getX(), points.get(0).getY());
                for (int i = points.size() - 1; i >= 0; --i) {
                    context.lineTo(points.get(i).getX(), points.get(i).getY());
                }
                break;
            case RECTANGLE:
                Rectangle rectangle = shape.getAsRectangle();
                context.rect(rectangle.getStart().getX(),
                        rectangle.getStart().getY(),
                        rectangle.getSize().getWidth(),
                        rectangle.getSize().getHeight());
                break;
        }
    }

    @Override
    public void drawImage(String src, Rectangle to) {
        HTMLImageElement element = imageCaches.get(src);
        if (element.complete) {
            context.drawImage(element,
                    to.getStart().getX(),
                    to.getStart().getY(),
                    to.getSize().getWidth(),
                    to.getSize().getHeight());
        }
    }

    @Override
    public void setFont(String name, int size, boolean italic, boolean bold) {
        context.font = (italic ? "italic " : "") + (bold ? "bold " : "") + size + "px " + name;
    }

    @Override
    public void fillText(String text, Point to) {
        context.fillText(text, to.getX(), to.getY());
    }

    @Override
    public int textWidth(String text) {
        return (int) context.measureText(text).width;
    }

    @Override
    public void clip(Shape shape) {
        clips.push(shape.getBoundingRectangle().intersect(getBoundingRectangle()));

        context.save();

        setShape(shape);

        context.clip();
    }

    @Override
    public void clipInverse(Shape shape) {
        Rectangle boundRect = clips.peek();
        clips.push(boundRect);

        context.save();

        setShape(shape);
        context.rect(context.canvas.width,
                0,
                -context.canvas.width,
                context.canvas.height
        );

        context.clip();
    }

    @Override
    public void restore() {
        clips.pop();
        context.restore();
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
