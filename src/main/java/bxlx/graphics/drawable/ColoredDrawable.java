package bxlx.graphics.drawable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public class ColoredDrawable extends DrawableWrapper {
    private Color nowColor;
    private Color lastDrewColor;

    public ColoredDrawable(IDrawable wrapped, Color color) {
        super(wrapped);
        nowColor = color;
    }

    public ColoredDrawable setColor(Color color) {
        this.nowColor = color;
        return this;
    }

    public Color getColor() {
        return nowColor;
    }

    protected Color getLastDrewColor() {
        return lastDrewColor;
    }

    protected void drewColor() {
        lastDrewColor = nowColor;
    }

    @Override
    public boolean needRedraw() {
        return super.needRedraw() || nowColor != lastDrewColor;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        canvas.setColor(nowColor);
        if (nowColor != lastDrewColor || !super.needRedraw()) {
            drewColor();
            super.forceDraw(canvas);
        } else {
            getWrapped().draw(canvas);
        }
    }
}
