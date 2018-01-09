package bxlx.graphics.container;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.IDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.02.07..
 */
public abstract class SizeChangeableContainer<T extends IDrawable, Myself extends SizeChangeableContainer<T, Myself>> extends DrawableContainer<T> {
    public SizeChangeableContainer() {
        this(new ArrayList<>());
    }

    public SizeChangeableContainer(List<T> children) {
        super(children);
    }

    @Override
    public Myself add(T elem) {
        super.add(elem);
        return getThis();
    }

    @Override
    public Myself add(Supplier<T> elem) {
        super.add(elem);
        return getThis();
    }

    @Override
    public ChangeableDrawable.ChangeableValue<T> get(int index) {
        return super.get(index);
    }

    @Override
    public int size() {
        return super.size();
    }

    public abstract Myself getThis();
}
