package bxlx.graphics.drawable;

import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.18..
 */
public class ZoomDrawable<T extends IDrawable> extends ClippedDrawable<T> {
    private final ChangeableValue<Double> zoom;
    private final ChangeableValue<Double> shiftX;
    private final ChangeableValue<Double> shiftY;

    public ZoomDrawable(T wrapped, boolean fake, Supplier<Double> zoom, Supplier<Double> x, Supplier<Double> y) {
        super(wrapped, fake, null);

        this.zoom = new ChangeableValue<>(this, zoom);
        this.shiftX = new ChangeableValue<>(this, x);
        this.shiftY = new ChangeableValue<>(this, y);

        setClip();
    }

    private void setClip() {
        getClip().setElem(r -> {
            Size size = r.getSize().asPoint().multiple(zoom.get()).asSize();

            Point start = r.getStart().add(r.getSize().asPoint().negate().multiple(new Point(-shiftX.get(), -shiftY.get())));
            return new Rectangle(start, size);
        });
    }
}
