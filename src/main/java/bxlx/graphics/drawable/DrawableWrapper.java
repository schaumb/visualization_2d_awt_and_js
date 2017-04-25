package bxlx.graphics.drawable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.13..
 */
public abstract class DrawableWrapper<T extends IDrawable> extends ChangeableDrawable {
    private ChangeableValue<T> child;

    public DrawableWrapper(T child) {
        this.child = new ChangeableValue<>(this, child);
    }

    public ChangeableValue<T> getChild() {
        return child;
    }


    @Override
    public Redraw needRedraw() {
        return super.needRedraw().orIf(true, child.get().needRedraw());
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        getChild().get().forceDraw(canvas);
    }

    @Override
    public void setRedraw() {
        super.setRedraw();
        getChild().get().setRedraw();
    }
}
