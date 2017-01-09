package bxlx.graphics.drawable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.IDrawable;

import java.util.List;

/**
 * Created by qqcs on 2017.01.09..
 */
public abstract class DrawableContainer extends ChangeableDrawable {
    protected final List<IDrawable> children;

    public DrawableContainer(List<IDrawable> children) {
        this.children = children;
    }

    @Override
    public boolean needRedraw() {
        boolean childNeedRedraw = false;
        for (IDrawable drawable : children) {
            childNeedRedraw |= drawable.needRedraw();
        }

        return super.needRedraw() || childNeedRedraw;
    }

    public boolean iChanged() {
        return super.needRedraw();
    }
}
