package bxlx.graphics.combined;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.fill.DrawArc;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.Text;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Magnifying extends ChangeableDrawable {
    private final DrawArc arc = DrawArc.circle(true);
    private final DrawRectangle clipRect = new DrawRectangle(0.8);
    private final ChangeableDrawable.ChangeableValue<Boolean> plus;

    public Magnifying(boolean plus) {
        this.plus = new ChangeableDrawable.ChangeableValue<>(this, plus);
    }

    public ChangeableDrawable.ChangeableValue<Boolean> getPlus() {
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