package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public abstract class DrawableWrapper implements IDrawable {
    private final IDrawable wrapped;

    public DrawableWrapper(IDrawable wrapped) {
        this.wrapped = wrapped;
    }

    public IDrawable getWrapped() {
        return wrapped;
    }

    @Override
    public boolean needRedraw() {
        return wrapped.needRedraw();
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        wrapped.forceDraw(canvas);
    }
}
