package bxlx.graphics;

import bxlx.system.functional.ValueOrSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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

    public static class ChangeableDependentValue<T, U> extends ChangeableValue<T> {
        private final ChangeableValue<U> dependent;
        private final ChangeableValue<Function<U, T>> depFun;
        public ChangeableDependentValue(ChangeableDrawable parent, Function<U, T> fun) {
            super(parent, (T) null);
            dependent = new ChangeableValue<>(parent, (U) null);
            depFun = new ChangeableValue<>(parent, fun);
            setSupplier(() -> depFun.get().apply(dependent.get()));
        }

        public ChangeableDependentValue<T, U> setDep(U dep) {
            dependent.setElem(dep);
            return this;
        }

        public ChangeableDependentValue<T, U> setDepSup(Supplier<U> sup) {
            dependent.setSupplier(sup);
            return this;
        }

        public ChangeableDependentValue<T, U> setDepFun(Function<U, T> fun) {
            depFun.setElem(fun);
            return this;
        }
    }

    private boolean redraw = false;
    protected List<ChangeableValue> values = new ArrayList<>();

    @Override
    public void setRedraw() {
        redraw = true;
    }

    public boolean isManuallyRedraw() {
        return redraw;
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

    private static String indent = "";

    @Override
    public final void forceDraw(ICanvas canvas) {
        /* Test what redraw
        boolean needIndent = false;
        if (!Splitter.class.isInstance(this) &&
                !Container.class.isInstance(this) &&
                !VisibleDrawable.class.isInstance(this) &&
                !AspectRatioDrawable.class.isInstance(this) &&
                !MarginDrawable.class.isInstance(this) &&
                !ClippedDrawable.class.isInstance(this) &&
                // !ColoredDrawable.class.isInstance(this) &&
                true) {
            String msg = "";
            if (Text.class.isInstance(this)) {
                msg += ((Text) this).getText().get();
            }
            if(ColoredDrawable.class.isInstance(this)) {
                msg += ((ColoredDrawable) this).getColor().get().toString();
            }
            msg = msg + this.needRedraw().parentNeedRedraw() + this.needRedraw().iNeedRedraw() + this.needRedraw().childNeedRedraw() + " vcs:" + valueChanged();
            SystemSpecific.get().log(indent + "ForceRedraw " + this.getClass().getName() + " " + msg);
            needIndent = true;
            indent += "  ";
        }
        */
        forceRedraw(canvas);
        redraw = false;

        for (ChangeableValue changeableValue : values) {
            changeableValue.commit();
        }
        /*
        if(needIndent) {
            indent = indent.substring(2);
        }
        */
    }

    protected abstract void forceRedraw(ICanvas canvas);
}
