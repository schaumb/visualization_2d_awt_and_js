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
    private final ChangeableDrawable.ChangeableValue<Boolean> inside;
    private final ChangeableDrawable.ChangeableValue<Boolean> ellipse;
    private final ChangeableDrawable.ChangeableValue<Integer> n;
    private final ChangeableDrawable.ChangeableValue<Double> startAngle;
    private final ChangeableDrawable.ChangeableValue<Boolean> makeCentered;

    public DrawNGon(boolean inside, boolean ellipse, int n, double startAngle, boolean makeCentered) {
        this.inside = new ChangeableDrawable.ChangeableValue<>(this, inside);
        this.ellipse = new ChangeableDrawable.ChangeableValue<>(this, ellipse);
        this.n = new ChangeableDrawable.ChangeableValue<>(this, n);
        this.startAngle = new ChangeableDrawable.ChangeableValue<>(this, startAngle);
        this.makeCentered = new ChangeableDrawable.ChangeableValue<>(this, makeCentered);
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

    public ChangeableDrawable.ChangeableValue<Boolean> getInside() {
        return inside;
    }

    public ChangeableDrawable.ChangeableValue<Boolean> getEllipse() {
        return ellipse;
    }

    public ChangeableDrawable.ChangeableValue<Integer> getN() {
        return n;
    }

    public ChangeableDrawable.ChangeableValue<Double> getStartAngle() {
        return startAngle;
    }

    public ChangeableDrawable.ChangeableValue<Boolean> getMakeCentered() {
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
