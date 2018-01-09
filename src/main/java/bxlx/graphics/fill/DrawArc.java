package bxlx.graphics.fill;

import bxlx.graphics.Drawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.system.ObservableValue;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawArc extends Drawable {
    private ObservableValue<Boolean> inside;
    private ObservableValue<Double> fromRadius;
    private ObservableValue<Double> toRadius;

    public DrawArc(boolean inside, double fromRadius, double toRadius) {
        this(new ObservableValue<>(inside), new ObservableValue<>(fromRadius), new ObservableValue<>(toRadius));
    }

    public DrawArc(ObservableValue<Boolean> inside, ObservableValue<Double> fromRadius, ObservableValue<Double> toRadius) {
        this.inside = inside;
        this.fromRadius = fromRadius;
        this.toRadius = toRadius;

        inside.addObserver((observable, from) -> setRedraw());
        fromRadius.addObserver((observable, from) -> setRedraw());
        toRadius.addObserver((observable, from) -> setRedraw());
    }

    public DrawArc(double fromRadius, double toRadius) {
        this(true, fromRadius, toRadius);
    }

    public static DrawArc circle(boolean inside) {
        return new DrawArc(inside, 0, 2 * Math.PI);
    }

    @Override
    protected void forceDraw(ICanvas canvas) {
        Size size = canvas.getBoundingRectangle().getSize();
        double radius = (inside.get() ? size.getShorterDimension() : size.getLongerDimension()) / 2;

        canvas.fill(new Arc(canvas.getBoundingRectangle().getCenter(), radius, fromRadius.get(), toRadius.get()));
    }
}
