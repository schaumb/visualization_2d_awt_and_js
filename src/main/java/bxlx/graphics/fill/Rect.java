package bxlx.graphics.fill;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2016.12.24..
 */
public class Rect implements IDrawable {
    private double rate;
    private Color color;

    public Rect() {
        this(1 / 3.0);
    }

    public Rect(double rate) {
        this.rate = rate;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(ICanvas canvas) {
        Rectangle bounds = canvas.getBoundingRectangle();

        if (bounds.getSize().getWidth() <= 0 || bounds.getSize().getHeight() <= 0) {
            return;
        }

        double circles = bounds.getSize().getShorterDimension() * rate;

        if (color != null) {
            canvas.setColor(color);
        }

        canvas.fill(Arc.circle(bounds.getStart().add(circles), circles));
        canvas.fill(Arc.circle(bounds.getStart().add(new Point(circles, bounds.getSize().getHeight() - circles)), circles));
        canvas.fill(Arc.circle(bounds.getStart().add(new Point(bounds.getSize().getWidth() - circles, circles)), circles));
        canvas.fill(Arc.circle(bounds.getStart().add(new Point(bounds.getSize().getWidth() - circles, bounds.getSize().getHeight() - circles)), circles));
        canvas.fill(new Rectangle(bounds.getStart().add(new Point(0, circles)),
                bounds.getStart().add(bounds.getSize().asPoint()).add(new Point(0, -circles))));
        canvas.fill(new Rectangle(bounds.getStart().add(new Point(circles, 0)),
                bounds.getStart().add(bounds.getSize().asPoint()).add(new Point(-circles, 0))));
    }

    public boolean isContains(Rectangle bounds, Point point) {
        if (bounds.getSize().getWidth() <= 0 || bounds.getSize().getHeight() <= 0) {
            return false;
        }

        double circles = bounds.getSize().getShorterDimension() * rate;

        return new Rectangle(bounds.getStart().add(new Point(circles, 0)), bounds.getStart().add(bounds.getSize().asPoint()).add(new Point(-circles, 0))).isContains(point)
                || new Rectangle(bounds.getStart().add(new Point(0, circles)), bounds.getStart().add(bounds.getSize().asPoint()).add(new Point(0, -circles))).isContains(point)
                || Arc.circle(bounds.getStart().add(circles), circles).isContains(point)
                || Arc.circle(bounds.getStart().add(new Point(circles, bounds.getSize().getHeight() - circles)), circles).isContains(point)
                || Arc.circle(bounds.getStart().add(new Point(bounds.getSize().getWidth() - circles, circles)), circles).isContains(point)
                || Arc.circle(bounds.getStart().add(new Point(bounds.getSize().getWidth() - circles, bounds.getSize().getHeight() - circles)), circles).isContains(point);
    }
}
