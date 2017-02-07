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
    private final Function<F, U> transformFunction;

    public TransformerContainer(T container, Function<F, U> transformFunction) {
        super(container);
        this.transformFunction = transformFunction;
    }

    public TransformerContainer<F, U, T> addAndTransform(F elem) {
        getChild().get().add(new ValueOrSupplier.Transform<F, U>().transform(new ValueOrSupplier<>(elem), transformFunction));
        return this;
    }

    public TransformerContainer<F, U, T> addAndTransform(Supplier<F> supplier) {
        getChild().get().add(new ValueOrSupplier.Transform<F, U>().transform(new ValueOrSupplier<>(supplier), transformFunction));
        return this;
    }

    public Function<F, U> getTransformFunction() {
        return transformFunction;
    }

    public ChangeableValue<U> getTransformed(int index) {
        return getChild().get().get(index);
    }

    @Override
    public int size() {
        return getChild().get().size();
    }
}
