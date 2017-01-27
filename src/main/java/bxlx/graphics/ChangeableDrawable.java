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

    private boolean redraw = false;
    protected List<ChangeableValue> values = new ArrayList<>();

    @Override
    public final void setRedraw() {
        redraw = true;
    }

    @Override
    public Redraw needRedraw() {
        return new Redraw().setIf(redraw || valueChanged(), Redraw.I_NEED_REDRAW);
    }

    private boolean valueChanged() {
        for (ChangeableValue changeableValue : values) {
            if (changeableValue.isChanged()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void forceDraw(ICanvas canvas) {
        /* Test what redraw
        if (!Splitter.class.isInstance(this) &&
                !Container.class.isInstance(this) &&
                !VisibleDrawable.class.isInstance(this) &&
                !AspectRatioDrawable.class.isInstance(this) &&
                !MarginDrawable.class.isInstance(this) &&
                !ClippedDrawable.class.isInstance(this) &&
                !ColoredDrawable.class.isInstance(this)) {
            String msg = "";
            if (Text.class.isInstance(this)) {
                msg += ((Text) this).getText().get();
            }
            if(ColoredDrawable.class.isInstance(this)) {
                msg += ((ColoredDrawable) this).getColor().get().toString();
            }
            SystemSpecific.get().log("ForceRedraw " + this.getClass().getName() + " " + msg);
        }
        */
        forceRedraw(canvas);
        redraw = false;

        for (ChangeableValue changeableValue : values) {
            changeableValue.commit();
        }
    }

    protected abstract void forceRedraw(ICanvas canvas);
}
