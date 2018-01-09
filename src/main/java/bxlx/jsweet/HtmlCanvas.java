package bxlx.jsweet;

import bxlx.graphics.Color;
import bxlx.graphics.Font;
import bxlx.graphics.ICanvas;
import bxlx.graphics.ImageCaches;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.graphics.shapes.Shape;
import def.dom.CanvasRenderingContext2D;
import def.dom.HTMLCanvasElement;
import def.dom.HTMLImageElement;
import jsweet.util.StringTypes;

import java.util.List;
import java.util.Stack;

import static def.dom.Globals.document;

/**
 * Created by qqcs on 2016.12.23..
 */
public class HtmlCanvas implements ICanvas {
    private final CanvasRenderingContext2D context;
    private final Stack<Rectangle> clips = new Stack<>();
    private final Stack<Object> patterns = new Stack<>();
    static final ImageCaches<HTMLImageElement> imageCaches =
            new ImageCaches<>(src -> {
                HTMLImageElement img = document.createElement(StringTypes.img);
                img.src = src;
                return img;
            });
    private Color latestColor;
    private Font latestFont = new Font("sans-serif", 10, false, false);

    public HtmlCanvas(HTMLCanvasElement canvasElement) {
        this.context = canvasElement.getContext(StringTypes._2d);
        clips.push(new Rectangle(Point.ORIGO, new Size(context.canvas.width, context.canvas.height)));
        patterns.push(context.fillStyle);
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return clips.peek();
    }

    @Override
    public void setColor(Color color) {
        if (color == null) return;
        latestColor = color;
        context.fillStyle = color.toString();

        context.globalAlpha = color.getAlpha();
    }

    @Override
    public Color getColor() {
        return latestColor;
    }

    @Override
    public void pushFillImg(String src, Size resizeImg) {
        HTMLImageElement element = imageCaches.get(src);
        if (element.complete) {
            HTMLCanvasElement tmpCanvasElement = document.createElement(StringTypes.canvas);
            tmpCanvasElement.width = resizeImg.getWidth();
            tmpCanvasElement.height = resizeImg.getHeight();
            CanvasRenderingContext2D tmpCanvas = tmpCanvasElement.getContext(StringTypes._2d);
            tmpCanvas.drawImage(element, 0, 0, resizeImg.getWidth(), resizeImg.getHeight());

            context.fillStyle = context.createPattern(tmpCanvasElement, "repeat");
            patterns.push(context.fillStyle);
        }
    }

    @Override
    public void popFillImg() {
        patterns.pop();
        context.fillStyle = patterns.peek();
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
    public void setFont(Font font) {
        latestFont = font;
        context.font = (font.isItalic() ? "italic " : "") + (font.isBold() ? "bold " : "") + font.getSize() + "px " + font.getName();
    }

    @Override
    public Font getFont() {
        return latestFont;
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
