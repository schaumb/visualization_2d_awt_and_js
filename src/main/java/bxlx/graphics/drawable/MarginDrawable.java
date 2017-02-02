package bxlx.graphics.drawable;

import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Function;

/**
 * Created by qqcs on 2017.01.09..
 */
public class MarginDrawable<T extends IDrawable> extends ClippedDrawable<T> {
    private final ChangeableValue<Function<Rectangle, Double>> marginX; // < 1 -> percent, >=1 -> pixel
    private final ChangeableValue<Function<Rectangle, Double>> marginY; // < 1 -> percent, >=1 -> pixel

    private void setTheClip() {
        getClip().setElem(rectangle -> {
            double nowMarginX = marginX.get().apply(rectangle);
            double nowMarginY = marginY.get().apply(rectangle);
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
        this.marginX = new ChangeableValue<>(this, marginX);
        this.marginY = new ChangeableValue<>(this, marginY);
        setTheClip();
    }

    public MarginDrawable(T wrapped) {
        this(wrapped, 0, 0);
    }

    public ChangeableValue<Function<Rectangle, Double>> getMarginX() {
        return marginX;
    }

    public ChangeableValue<Function<Rectangle, Double>> getMarginY() {
        return marginY;
    }
}
