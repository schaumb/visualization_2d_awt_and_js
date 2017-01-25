package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Magnifying extends ChangeableDrawable {
    private final DrawArc arc = DrawArc.circle(true);
    private final DrawRectangle clipRect = new DrawRectangle(0.8);
    private final ChangeableValue<Boolean> plus;

    public Magnifying(boolean plus) {
        this.plus = new ChangeableValue<>(this, plus);
    }

    public ChangeableValue<Boolean> getPlus() {
        return plus;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        arc.forceDraw(canvas);

        canvas.clip(clipRect.getRectangle(canvas.getBoundingRectangle()));
        Color color = canvas.getColor();
        canvas.setColor(Color.WHITE);
        arc.forceDraw(canvas);
        canvas.restore();
        canvas.setColor(color);
        new Text(plus.get() ? "+" : "-", "+", 0).forceDraw(canvas);
    }
}