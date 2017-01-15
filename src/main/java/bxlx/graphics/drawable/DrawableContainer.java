package bxlx.graphics.drawable;

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
            this.children.add(new ChangeableValue<>(this, drawable));
        }
    }

    @Override
    public boolean needRedraw() {
        return iChanged() || childrenChanged();
    }

    public boolean iChanged() {
        return super.needRedraw();
    }

    protected void add(T elem) {
        children.add(new ChangeableValue<>(this, elem));
    }

    protected void add(Supplier<T> elem) {
        children.add(new ChangeableValue<>(this, elem));
    }

    protected ChangeableValue<T> get(int index) {
        return children.get(index);
    }

    protected int size() {
        return children.size();
    }

    @Override
    public void setOnlyForceDraw() {
        super.setOnlyForceDraw();
        for (ChangeableValue<T> drawable : children) {
            T draw = drawable.get();
            if (draw != null) {
                draw.setOnlyForceDraw();
            }
        }
    }

    public boolean childrenChanged() {
        boolean childNeedRedraw = false;
        for (ChangeableValue<T> drawable : children) {
            childNeedRedraw |= drawable.isChanged();
            T draw = drawable.get();
            if (draw != null) {
                childNeedRedraw |= draw.needRedraw();
            }
        }
        return childNeedRedraw;
    }
}
