package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.09..
 */
public class VisibleDrawable extends DrawableWrapper {
    private ChangeableValue<Boolean> visible;

    public VisibleDrawable(IDrawable wrapped, boolean visible) {
        super(wrapped);
        this.visible = new ChangeableValue<>(this, visible);
    }

    public VisibleDrawable(IDrawable wrapped, Supplier<Boolean> visible) {
        super(wrapped);
        this.visible = new ChangeableValue<>(this, visible);
    }

    public ChangeableValue<Boolean> getVisible() {
        return visible;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (!visible.get()) {
            return;
        }
        super.forceRedraw(canvas);
    }
}
