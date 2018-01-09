package bxlx.graphics.fill;

import bxlx.graphics.Drawable;
import bxlx.graphics.ICanvas;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawRectangle extends Drawable {
    @Override
    public void forceDraw(ICanvas canvas) {
        canvas.fill(canvas.getBoundingRectangle());
    }
}
