package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.18..
 */
public class FakeZoomDrawable extends FakeClippedDrawable {
    private final ChangeableValue<Double> zoom;
    private final ChangeableValue<Double> shiftX;
    private final ChangeableValue<Double> shiftY;

    public FakeZoomDrawable(IDrawable wrapped) {
        this(wrapped, 1, 0, 0);
    }

    public FakeZoomDrawable(IDrawable wrapped, double zoom, double x, double y) {
        super(wrapped, null);

        this.zoom = new ChangeableValue<>(this, zoom);
        this.shiftX = new ChangeableValue<>(this, x);
        this.shiftY = new ChangeableValue<>(this, y);

        setClip();
    }

    public FakeZoomDrawable(IDrawable wrapped, Supplier<Double> zoom, Supplier<Double> x, Supplier<Double> y) {
        super(wrapped, null);

        this.zoom = new ChangeableValue<>(this, zoom);
        this.shiftX = new ChangeableValue<>(this, x);
        this.shiftY = new ChangeableValue<>(this, y);

        setClip();
    }

    private void setClip() {
        getClip().setElem(r -> {
            Size size = r.getSize().asPoint().multiple(zoom.get()).asSize();

            Point start = r.getStart().add(r.getSize().asPoint().negate().multiple(new Point(shiftX.get(), shiftY.get())));
            return new Rectangle(start, size);
        });
    }
}
