package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.container.DrawableContainer;

import java.util.Collections;

/**
 * Created by qqcs on 2017.01.13..
 */
public abstract class DrawableWrapper<T extends IDrawable> extends DrawableContainer<T> {
    public DrawableWrapper(T child) {
        super(Collections.singletonList(child));
    }

    public ChangeableValue<T> getChild() {
        return get(0);
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

    @Override
    protected boolean parentRedrawSatisfy() {
        return false;
    }
}
