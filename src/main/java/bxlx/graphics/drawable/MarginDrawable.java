package bxlx.graphics.drawable;

import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Function;

/**
 * Created by qqcs on 2017.01.09..
 */
public class MarginDrawable<T extends IDrawable> extends ClippedDrawable<T> {
    private final ChangeableDependentValue<Double, Rectangle> marginX; // < 1 -> percent, >=1 -> pixel
    private final ChangeableDependentValue<Double, Rectangle> marginY; // < 1 -> percent, >=1 -> pixel

    private void setTheClip() {
        getClip().setElem(rectangle -> {
            double nowMarginX = marginX.setDep(rectangle).get();
            double nowMarginY = marginY.setDep(rectangle).get();
            Point pixel = new Point(
                    nowMarginX >= 1 ? nowMarginX : nowMarginX * rectangle.getSize().getWidth() / 2,
                    nowMarginY >= 1 ? nowMarginY : nowMarginY * rectangle.getSize().getHeight() / 2
            );
            return new Rectangle(rectangle.getStart().add(pixel),
                    rectangle.getEnd().add(pixel.negate()));
        });
    }

    public MarginDrawable(T wrapped, double marginX, double marginY) {
        this(wrapped, r -> marginX, r -> marginY);
    }

    public MarginDrawable(T drawable, Function<Rectangle, Double> marginX, Function<Rectangle, Double> marginY) {
        super(drawable, false, null);
        this.marginX = new ChangeableDependentValue<>(this, marginX);
        this.marginY = new ChangeableDependentValue<>(this, marginY);
        setTheClip();
    }

    public MarginDrawable(T wrapped) {
        this(wrapped, 0, 0);
    }

    public ChangeableDependentValue<Double, Rectangle> getMarginX() {
        return marginX;
    }

    public ChangeableDependentValue<Double, Rectangle> getMarginY() {
        return marginY;
    }
}
