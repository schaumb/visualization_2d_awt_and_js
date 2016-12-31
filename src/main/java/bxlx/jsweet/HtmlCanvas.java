package bxlx.jsweet;

import bxlx.graphics.ImageCaches;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
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
    private static final ImageCaches<HTMLImageElement> imageCaches =
            new ImageCaches<>(src -> {
                HTMLImageElement img = document.createElement(StringTypes.img);
                img.src = src;
                return img;
            });
    private Color latestColor;

    public HtmlCanvas(HTMLCanvasElement canvasElement) {
        this.context = canvasElement.getContext(StringTypes._2d);
    }

    @Override
    public Rectangle getBoundingRectangle() {
        if(clips.empty()) {
            return new Rectangle(Point.ORIGO, new Size(context.canvas.width, context.canvas.height));
        }
        return clips.peek();
    }

    @Override
    public void setColor(Color color) {
        if(color == null) return;
        latestColor = color;
        context.fillStyle = union(color.toString());
        context.globalAlpha = color.getAlpha();
    }

    @Override
    public Color getColor() {
        return latestColor;
    }

    @Override
    public void fillArc(Arc arc) {
        context.beginPath();
        context.moveTo(arc.getCenter().getX(), arc.getCenter().getY());
        context.arc(arc.getCenter().getX(), arc.getCenter().getY(),
                arc.getRadius(), Math.PI * 2 - arc.getFromRadian(), Math.PI * 2 - arc.getToRadian(),
                arc.getFromRadian() < arc.getToRadian());
        context.fill();
    }

    @Override
    public void fillRectangle(Rectangle rectangle) {
        context.fillRect(rectangle.getStart().getX(),
                rectangle.getStart().getY(),
                rectangle.getSize().getWidth(),
                rectangle.getSize().getHeight());
    }

    @Override
    public void fillPolygon(Polygon polygon) {
        List<Point> points = polygon.getPoints();
        context.beginPath();
        context.moveTo(points.get(0).getX(), points.get(0).getY());
        for(int i = 1; i < points.size(); ++i) {
            context.lineTo(points.get(i).getX(), points.get(i).getY());
        }
        context.fill();
    }

    @Override
    public void drawImage(String src, Rectangle to) {
        HTMLImageElement element = imageCaches.get(src);
        if(element.complete) {
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
    public void clip(Rectangle rectangle) {
        clips.push(rectangle.intersect(getBoundingRectangle()));
        context.save();
        context.beginPath();
        context.rect(rectangle.getStart().getX(),
                rectangle.getStart().getY(),
                rectangle.getSize().getWidth(),
                rectangle.getSize().getHeight());
        context.clip();
    }

    @Override
    public void restore() {
        clips.pop();
        context.restore();
    }
}
