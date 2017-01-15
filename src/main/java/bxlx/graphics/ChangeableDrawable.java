package bxlx.graphics;

import bxlx.system.ValueOrSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.09..
 */
public abstract class ChangeableDrawable implements IDrawable {
    public static class ChangeableValue<T> extends ValueOrSupplier<T> {
        public ChangeableValue(ChangeableDrawable parent, Supplier<T> supplier) {
            super(supplier);
            parent.values.add(this);
        }

        public ChangeableValue(ChangeableDrawable parent, T elem) {
            super(elem);
            parent.values.add(this);
        }
    }

    private boolean onlyForceDraw = false;
    private boolean redraw = false;
    protected List<ChangeableValue> values = new ArrayList<>();

    public void setRedraw() {
        redraw = true;
    }

    @Override
    public boolean needRedraw() {
        return !onlyForceDraw && (redraw || valueChanged());
    }

    private boolean valueChanged() {
        boolean changed = false;
        for (ChangeableValue changeableValue : values) {
            changed |= changeableValue.isChanged();
        }
        return changed;
    }

    @Override
    public final void forceDraw(ICanvas canvas) {
        forceRedraw(canvas);
        redraw = false;
        onlyForceDraw = false;

        for (ChangeableValue changeableValue : values) {
            changeableValue.commit();
        }
    }

    @Override
    public void setOnlyForceDraw() {
        onlyForceDraw = true;
    }

    public boolean isOnlyForceDraw() {
        return onlyForceDraw;
    }

    protected abstract void forceRedraw(ICanvas canvas);
}
