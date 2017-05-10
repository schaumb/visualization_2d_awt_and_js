package bxlx.graphics.drawable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.shapes.Shape;

import java.util.function.Supplier;

/**
 * Created by qqcs on 5/10/17.
 */
public class ShapeClippedDrawable<T extends IDrawable> extends DrawableWrapper<T> {
    private final ChangeableValue<Shape> shape;

    public ShapeClippedDrawable(T drawable, Shape shape) {
        super(drawable);
        this.shape = new ChangeableValue<>(this, shape);
    }

    public ShapeClippedDrawable(T drawable, Supplier<Shape> shape) {
        super(drawable);
        this.shape = new ChangeableValue<>(this, shape);
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        canvas.clip(shape.get());

        super.forceRedraw(canvas);

        canvas.restore();
    }
}
