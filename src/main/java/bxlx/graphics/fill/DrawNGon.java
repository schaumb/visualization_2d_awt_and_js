package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Shape;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNGon extends ChangeableDrawable {
    private final ChangeableValue<Boolean> inside;
    private final ChangeableValue<Boolean> ellipse;
    private final ChangeableValue<Integer> n;
    private final ChangeableValue<Double> startAngle;
    private final ChangeableValue<Boolean> makeCentered;

    public DrawNGon(boolean inside, boolean ellipse, int n, double startAngle, boolean makeCentered) {
        this.inside = new ChangeableValue<>(this, inside);
        this.ellipse = new ChangeableValue<>(this, ellipse);
        this.n = new ChangeableValue<>(this, n);
        this.startAngle = new ChangeableValue<>(this, startAngle);
        this.makeCentered = new ChangeableValue<>(this, makeCentered);
    }

    public DrawNGon(int n, double startAngle, boolean makeCentered) {
        this(true, false, n, startAngle, makeCentered);
    }

    public DrawNGon(int n, double startAngle) {
        this(n, startAngle, false);
    }

    public DrawNGon(int n) {
        this(n, 0);
    }

    public ChangeableValue<Boolean> getInside() {
        return inside;
    }

    public ChangeableValue<Boolean> getEllipse() {
        return ellipse;
    }

    public ChangeableValue<Integer> getN() {
        return n;
    }

    public ChangeableValue<Double> getStartAngle() {
        return startAngle;
    }

    public ChangeableValue<Boolean> getMakeCentered() {
        return makeCentered;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        Size size = canvas.getBoundingRectangle().getSize();
        Point center = canvas.getBoundingRectangle().getCenter();

        Shape polygon;
        if (ellipse.get()) {
            polygon = Polygon.ellipseNGon(n.get(), center, size, startAngle.get());
        } else {
            double radius = (inside.get() ? size.getShorterDimension() : size.getLongerDimension()) / 2;
            polygon = Polygon.nGon(n.get(), center, radius, startAngle.get());
        }

        if (makeCentered.get()) {
            Point nowCenter = polygon.getBoundingRectangle().getCenter();
            polygon = polygon.getTranslated(center.add(nowCenter.negate()));
        }

        canvas.fill(polygon);
    }
}
