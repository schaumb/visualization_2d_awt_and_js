package bxlx.graphics.fill;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Magnifying implements IDrawable {
    private final DrawArc arc = DrawArc.circle(true);
    private final DrawRectangle clipRect = new DrawRectangle(0.8);
    private final Text text;

    public Magnifying(boolean plus) {
        this.text = new Text(plus ? "+" : "-", "+");
    }

    @Override
    public boolean needRedraw() {
        return false;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        arc.forceDraw(canvas);

        canvas.clip(clipRect.getRectangle(canvas.getBoundingRectangle()));
        Color color = canvas.getColor();
        canvas.setColor(Color.WHITE);
        arc.forceDraw(canvas);
        canvas.restore();
        canvas.setColor(color);
        text.forceDraw(canvas);
    }
}