package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.shapes.Shape;

/**
 * Created by qqcs on 5/11/17.
 */
public class HackClippedDrawable<T extends IDrawable> extends DrawableWrapper<T> {
    public HackClippedDrawable(T child) {
        super(child);
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        Shape wasClipped = canvas.getBoundingRectangle();
        canvas.restore();

        super.forceRedraw(canvas);

        canvas.clip(wasClipped);
    }
}
