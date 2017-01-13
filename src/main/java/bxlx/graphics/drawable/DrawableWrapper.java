package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

import java.util.Collections;

/**
 * Created by qqcs on 2017.01.13..
 */
public abstract class DrawableWrapper<T extends IDrawable> extends DrawableContainer<T> {
    public DrawableWrapper(T child) {
        super(Collections.singletonList(child));
    }

    public T getChild() {
        return children.get(0);
    }

    public DrawableWrapper<T> setChild(T child) {
        children.set(0, child);
        setRedraw();
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        getChild().forceDraw(canvas);
    }
}
