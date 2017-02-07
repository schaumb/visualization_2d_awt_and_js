package bxlx.graphics.container;

import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.system.functional.ValueOrSupplier;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.02.07..
 */
public class TransformerContainer<F extends IDrawable, U extends IDrawable, T extends DrawableContainer<U>> extends DrawableWrapper<T> {
    private final Function<F, U> function;

    public TransformerContainer(T container, Function<F, U> function) {
        super(container);
        this.function = function;
    }

    public TransformerContainer<F, U, T> addAndTransform(F elem) {
        getChild().get().add(new ValueOrSupplier.Transform<F, U>().transform(new ValueOrSupplier<>(elem), function));
        return this;
    }

    public TransformerContainer<F, U, T> addAndTransform(Supplier<F> supplier) {
        getChild().get().add(new ValueOrSupplier.Transform<F, U>().transform(new ValueOrSupplier<>(supplier), function));
        return this;
    }

    public ChangeableValue<U> getTransformed(int index) {
        return getChild().get().get(index);
    }

    @Override
    public int size() {
        return getChild().get().size();
    }
}
