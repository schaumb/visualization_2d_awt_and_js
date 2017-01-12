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
    private boolean inside;
    private boolean ellipse;
    private int n;
    private double startAngle;
    private boolean makeCentered;

    public DrawNGon(boolean inside, boolean ellipse, int n, double startAngle, boolean makeCentered) {
        this.inside = inside;
        this.ellipse = ellipse;
        this.n = n;
        this.startAngle = startAngle;
        this.makeCentered = makeCentered;
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

    public int getN() {
        return n;
    }

    public DrawNGon setN(int n) {
        this.n = n;
        setRedraw();
        return this;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public DrawNGon setStartAngle(double startAngle) {
        this.startAngle = startAngle;
        setRedraw();
        return this;
    }

    public boolean isInside() {
        return inside;
    }

    public DrawNGon setInside(boolean inside) {
        this.inside = inside;
        setRedraw();
        return this;
    }

    public boolean isEllipse() {
        return ellipse;
    }

    public DrawNGon setEllipse(boolean ellipse) {
        this.ellipse = ellipse;
        setRedraw();
        return this;
    }

    public boolean isMakeCentered() {
        return makeCentered;
    }

    public DrawNGon setMakeCentered(boolean makeCentered) {
        this.makeCentered = makeCentered;
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        Size size = canvas.getBoundingRectangle().getSize();
        Point center = canvas.getBoundingRectangle().getCenter();

        Shape polygon;
        if (ellipse) {
            polygon = Polygon.ellipseNGon(n, center, size, startAngle);
        } else {
            double radius = (inside ? size.getShorterDimension() : size.getLongerDimension()) / 2;
            polygon = Polygon.nGon(n, center, radius, startAngle);
        }

        if (makeCentered) {
            Point nowCenter = polygon.getBoundingRectangle().getCenter();
            polygon = polygon.getTranslated(center.add(nowCenter.negate()));
        }

        canvas.fill(polygon);
    }
}