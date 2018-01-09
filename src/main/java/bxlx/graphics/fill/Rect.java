package bxlx.graphics.fill;

import bxlx.graphics.Drawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ObservableValue;

/**
 * Created by qqcs on 2016.12.24..
 */
public class Rect extends Drawable {
    private final ObservableValue<Double> rate;

    public Rect() {
        this(1 / 3.0);
    }

    public Rect(double rate) {
        this.rate = new ObservableValue<>(rate);
        this.rate.addObserver((x, y) -> setRedraw());
    }

    public ObservableValue<Double> getRate() {
        return rate;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        Rectangle bounds = canvas.getBoundingRectangle();

        if (bounds.getSize().getWidth() <= 0 || bounds.getSize().getHeight() <= 0) {
            return;
        }

        double circles = bounds.getSize().getShorterDimension() * rate.get();

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

        double circles = bounds.getSize().getShorterDimension() * rate.get();

        return new Rectangle(bounds.getStart().add(new Point(circles, 0)), bounds.getStart().add(bounds.getSize().asPoint()).add(new Point(-circles, 0))).isContains(point)
                || new Rectangle(bounds.getStart().add(new Point(0, circles)), bounds.getStart().add(bounds.getSize().asPoint()).add(new Point(0, -circles))).isContains(point)
                || Arc.circle(bounds.getStart().add(circles), circles).isContains(point)
                || Arc.circle(bounds.getStart().add(new Point(circles, bounds.getSize().getHeight() - circles)), circles).isContains(point)
                || Arc.circle(bounds.getStart().add(new Point(bounds.getSize().getWidth() - circles, circles)), circles).isContains(point)
                || Arc.circle(bounds.getStart().add(new Point(bounds.getSize().getWidth() - circles, bounds.getSize().getHeight() - circles)), circles).isContains(point);
    }
}
