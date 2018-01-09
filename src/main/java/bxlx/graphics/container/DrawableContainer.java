package bxlx.graphics.container;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.IDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.09..
 */
public abstract class DrawableContainer<T extends IDrawable> extends ChangeableDrawable {
    private final List<ChangeableValue<T>> children = new ArrayList<>();

    public DrawableContainer() {
    }

    public DrawableContainer(List<T> children) {
        for (T drawable : children) {
            this.children.add(new ChangeableDrawable.ChangeableValue<>(this, drawable));
        }
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().orIf(true, childrenChanged());
    }

    protected DrawableContainer<T> add(T elem) {
        children.add(new ChangeableDrawable.ChangeableValue<>(this, elem));
        return this;
    }

    protected DrawableContainer<T> add(Supplier<T> elem) {
        children.add(new ChangeableDrawable.ChangeableValue<>(this, elem));
        return this;
    }

    protected ChangeableDrawable.ChangeableValue<T> get(int index) {
        return children.get(index);
    }

    protected int size() {
        return children.size();
    }

    protected IDrawable.Redraw childrenChanged() {
        boolean parent = parentRedrawSatisfy();
        IDrawable.Redraw result = new IDrawable.Redraw();
        for (ChangeableValue<T> drawable : children) {
            T child = drawable.get();
            if (child != null) {
                IDrawable.Redraw childNeedRedraw = child.needRedraw();

                if (parent && childNeedRedraw.needRedraw()) {
                    return new IDrawable.Redraw(Redraw.CHILD_NEED_REDRAW);
                } else if (childNeedRedraw.needRedraw()) {
                    if (childNeedRedraw.parentNeedRedraw()) {
                        result.setParentNeedRedraw();
                    }
                    if (childNeedRedraw.needRedrawExcept(Redraw.PARENT_NEED_REDRAW)) {
                        result.setChildNeedRedraw();
                    }
                }
            }
        }
        return result;
    }

    protected abstract boolean parentRedrawSatisfy();
}
