package bxlx.graphics.drawable;

import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.18..
 */
public class ZoomDrawable<T extends IDrawable> extends ClippedDrawable<T> {
    private final ChangeableValue<Function<Rectangle, Double>> zoom;
    private final ChangeableValue<Function<Rectangle, Double>> shiftX;
    private final ChangeableValue<Function<Rectangle, Double>> shiftY;

    public ZoomDrawable(T wrapped, boolean fake, Function<Rectangle, Double> zoom, Function<Rectangle, Double> x, Function<Rectangle, Double> y) {
        super(wrapped, fake, null);

        this.zoom = new ChangeableValue<>(this, zoom);
        this.shiftX = new ChangeableValue<>(this, x);
        this.shiftY = new ChangeableValue<>(this, y);

        setClip();
    }

    public ZoomDrawable(T wrapped, boolean fake, Supplier<Double> zoom, Supplier<Double> x, Supplier<Double> y) {
        super(wrapped, fake, null);

        this.zoom = new ChangeableValue<>(this, r -> zoom.get());
        this.shiftX = new ChangeableValue<>(this, r -> x.get());
        this.shiftY = new ChangeableValue<>(this, r -> y.get());

        setClip();
    }

    private void setClip() {
        getClip().setElem(r -> {
            Size size = r.getSize().asPoint().multiple(zoom.get().apply(r)).asSize();

            Point start = r.getStart().add(r.getSize().asPoint().negate().multiple(new Point(-shiftX.get().apply(r), -shiftY.get().apply(r))));
            return new Rectangle(start, size);
        });
    }
}
