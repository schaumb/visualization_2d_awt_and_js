package bxlx.graphics.drawable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public class ColoredDrawable extends DrawableWrapper {
    private Color nowColor;

    public ColoredDrawable(IDrawable wrapped, Color color) {
        super(wrapped);
        nowColor = color;
    }

    public ColoredDrawable setColor(Color color) {
        this.nowColor = color;
        setRedraw();
        return this;
    }

    public Color getColor() {
        return nowColor;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        Color tmp = canvas.getColor();
        canvas.setColor(nowColor);
        super.forceRedraw(canvas);
        canvas.setColor(tmp);
    }
}
