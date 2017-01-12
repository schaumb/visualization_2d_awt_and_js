package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public abstract class DrawableWrapper<T extends IDrawable> implements IDrawable {
    private final T wrapped;

    public DrawableWrapper(T wrapped) {
        this.wrapped = wrapped;
    }

    public T getWrapped() {
        return wrapped;
    }

    @Override
    public boolean needRedraw() {
        return wrapped.needRedraw();
    }

    @Override
    public void setOnlyForceDraw() {
        wrapped.setOnlyForceDraw();
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        wrapped.forceDraw(canvas);
    }
}
