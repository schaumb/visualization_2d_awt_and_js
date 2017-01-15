package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawArc extends ChangeableDrawable {
    private ChangeableValue<Boolean> inside;
    private ChangeableValue<Double> fromRadius;
    private ChangeableValue<Double> toRadius;

    public DrawArc(boolean inside, double fromRadius, double toRadius) {
        this.inside = new ChangeableValue<>(this, inside);
        this.fromRadius = new ChangeableValue<>(this, fromRadius);
        this.toRadius = new ChangeableValue<>(this, toRadius);
        setRedraw();
    }

    public DrawArc(double fromRadius, double toRadius) {
        this(true, fromRadius, toRadius);
    }

    public static DrawArc circle(boolean inside) {
        return new DrawArc(inside, 0, 2 * Math.PI);
    }


    @Override
    protected void forceRedraw(ICanvas canvas) {
        Size size = canvas.getBoundingRectangle().getSize();
        double radius = (inside.get() ? size.getShorterDimension() : size.getLongerDimension()) / 2;

        canvas.fill(new Arc(canvas.getBoundingRectangle().getCenter(), radius, fromRadius.get(), toRadius.get()));
    }
}
