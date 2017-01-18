package bxlx.graphics.drawable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.09..
 */
public class ColoredDrawable<T extends IDrawable> extends DrawableWrapper<T> {
    private final ChangeableValue<Color> color;

    public ColoredDrawable(T wrapped, Color color) {
        super(wrapped);
        this.color = new ChangeableValue<>(this, color);
    }

    public ColoredDrawable(T wrapped, Supplier<Color> color) {
        super(wrapped);
        this.color = new ChangeableValue<>(this, color);
    }

    public ChangeableValue<Color> getColor() {
        return color;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        Color tmp = canvas.getColor();
        canvas.setColor(color.get());
        super.forceRedraw(canvas);
        canvas.setColor(tmp);
    }
}
