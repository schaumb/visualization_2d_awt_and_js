package bxlx.graphics.drawable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.shapes.Shape;

import java.util.function.Supplier;

/**
 * Created by qqcs on 5/8/17.
 */
public class ShapeDrawable extends ChangeableDrawable {
    private final ChangeableValue<Shape> shape;

    public ShapeDrawable(Shape shape) {
        this.shape = new ChangeableValue<>(this, shape);
    }

    public ShapeDrawable(Supplier<Shape> shape) {
        this.shape = new ChangeableValue<>(this, shape);
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        canvas.fill(shape.get());
    }
}
