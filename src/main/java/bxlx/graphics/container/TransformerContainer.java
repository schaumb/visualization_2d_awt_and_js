package bxlx.graphics.container;

import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.system.functional.ValueOrSupplier;

import java.util.ArrayList;
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

    public TransformerContainer<F, U, T> add(F elem) {
        getChild().get().add(new ChangeableValue<>(this, elem).transform(transformFunction).getAsSupplier());
        return this;
    }

    public TransformerContainer<F, U, T> add(Supplier<F> supplier) {
        getChild().get().add(new ChangeableValue<>(this, supplier).transform(transformFunction).getAsSupplier());
        return this;
    }

    public TransformerContainer<F, U, T> add(ArrayList<F> elems) {
        for (F elem : elems) {
            getChild().get().add(new ChangeableValue<>(this, elem).transform(transformFunction).getAsSupplier());
        }
        return this;
    }

    public TransformerContainer<F, U, T> addVal(ChangeableValue<F> changeable) {
        getChild().get().add(changeable.transform(transformFunction).getAsSupplier());
        return this;
    }

    public Function<F, U> getTransformFunction() {
        return transformFunction;
    }

    public ChangeableValue<U> get(int index) {
        return getChild().get().get(index);
    }

    public int size() {
        return getChild().get().size();
    }
}
