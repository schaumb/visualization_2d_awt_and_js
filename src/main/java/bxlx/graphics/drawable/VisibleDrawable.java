package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public class VisibleDrawable extends DrawableWrapper {
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
        setRedraw();
        return this;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (!visible) {
            return;
        }
        super.forceRedraw(canvas);
    }
}
