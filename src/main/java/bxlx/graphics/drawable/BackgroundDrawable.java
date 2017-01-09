package bxlx.graphics.drawable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public class BackgroundDrawable extends ColoredDrawable {
    public BackgroundDrawable(IDrawable wrapped, Color color) {
        super(wrapped, color);
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        canvas.clearCanvas(getColor());
        drewColor();
        super.forceDraw(canvas);
    }
}
