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
    private final ChangeableDependentValue<Double, Rectangle> zoom;
    private final ChangeableDependentValue<Double, Rectangle> shiftX;
    private final ChangeableDependentValue<Double, Rectangle> shiftY;

    public ZoomDrawable(T wrapped, boolean fake, Function<Rectangle, Double> zoom, Function<Rectangle, Double> x, Function<Rectangle, Double> y) {
        super(wrapped, fake, null);

        this.zoom = new ChangeableDependentValue<>(this, zoom);
        this.shiftX = new ChangeableDependentValue<>(this, x);
        this.shiftY = new ChangeableDependentValue<>(this, y);

        setClip();
    }

    public ZoomDrawable(T wrapped, boolean fake, Supplier<Double> zoom, Supplier<Double> x, Supplier<Double> y) {
        this(wrapped, fake, r -> zoom.get(), r -> x.get(), r -> y.get());
    }

    private void setClip() {
        getClip().setElem(r -> {
            Size size = r.getSize().asPoint().multiple(zoom.setDep(r).get()).asSize();

            Point start = r.getStart().add(r.getSize().asPoint().negate().multiple(new Point(-shiftX.setDep(r).get(),
                    -shiftY.setDep(r).get())));
            return new Rectangle(start, size);
        });
    }
}
