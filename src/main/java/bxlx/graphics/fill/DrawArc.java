package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawArc extends ChangeableDrawable {
    private boolean inside;
    private double fromRadius;
    private double toRadius;

    public DrawArc(boolean inside, double fromRadius, double toRadius) {
        this.inside = inside;
        this.fromRadius = fromRadius;
        this.toRadius = toRadius;
        setRedraw();
    }

    public DrawArc(double fromRadius, double toRadius) {
        this(true, fromRadius, toRadius);
    }

    public static DrawArc circle() {
        return new DrawArc(0, 2 * Math.PI);
    }

    public double getFromRadius() {
        return fromRadius;
    }

    public DrawArc setFromRadius(double fromRadius) {
        this.fromRadius = fromRadius;
        setRedraw();
        return this;
    }

    public double getToRadius() {
        return toRadius;
    }

    public DrawArc setToRadius(double toRadius) {
        this.toRadius = toRadius;
        setRedraw();
        return this;
    }

    public boolean isInside() {
        return inside;
    }

    public DrawArc setInside(boolean inside) {
        this.inside = inside;
        setRedraw();
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        Size size = canvas.getBoundingRectangle().getSize();
        double radius = (inside ? size.getShorterDimension() : size.getLongerDimension()) / 2;

        canvas.fill(new Arc(canvas.getBoundingRectangle().getCenter(), radius, fromRadius, toRadius));
    }
}
