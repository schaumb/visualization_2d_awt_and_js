package bxlx.graphics.drawable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.IDrawable;

import java.util.List;

/**
 * Created by qqcs on 2017.01.09..
 */
public abstract class DrawableContainer<T extends IDrawable> extends ChangeableDrawable {
    protected final List<T> children;

    public DrawableContainer(List<T> children) {
        this.children = children;
    }

    @Override
    public boolean needRedraw() {
        return iChanged() || childrenChanged();
    }

    public boolean iChanged() {
        return super.needRedraw();
    }

    @Override
    public void setOnlyForceDraw() {
        super.setOnlyForceDraw();
        for (IDrawable drawable : children) {
            if (drawable != null) {
                drawable.setOnlyForceDraw();
            }
        }
    }

    public boolean childrenChanged() {
        boolean childNeedRedraw = false;
        for (IDrawable drawable : children) {
            if (drawable != null) {
                childNeedRedraw |= drawable.needRedraw();
            }
        }
        return childNeedRedraw;
    }
}
