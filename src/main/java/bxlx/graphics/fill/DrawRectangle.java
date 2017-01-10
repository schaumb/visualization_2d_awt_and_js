package bxlx.graphics.fill;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawRectangle implements IDrawable {
    @Override
    public boolean needRedraw() {
        return false;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        canvas.fill(canvas.getBoundingRectangle());
    }
}
