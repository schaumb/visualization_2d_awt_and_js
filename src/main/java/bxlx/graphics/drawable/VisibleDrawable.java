package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public class VisibleDrawable extends DrawableWrapper {
    private boolean lastWasVisible = true;
    private boolean visible;

    public VisibleDrawable(IDrawable wrapped, boolean visible) {
        super(wrapped);
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public VisibleDrawable setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean needRedraw() {
        return visible ^ lastWasVisible || (visible && super.needRedraw());
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        if (!visible) {
            lastWasVisible = false;
            return;
        }

        if (!lastWasVisible || !super.needRedraw()) {
            super.forceDraw(canvas);
        } else {
            getWrapped().draw(canvas);
        }
        lastWasVisible = true;
    }
}
