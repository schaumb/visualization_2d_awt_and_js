package bxlx.graphics.drawable_helper;

import bxlx.graphics.DrawableContainer;
import bxlx.graphics.ICanvas;
import bxlx.graphics.shapes.Shape;
import bxlx.system.ObservableValue;

public class ShapeClipper extends CanvasChanger {
    private final ObservableValue<Shape> shape;

    ShapeClipper(CanvasChanger andThen, ObservableValue<Shape> shape) {
        super(andThen);
        this.shape = shape;

        shape.addObserver((observable, from) -> notifyObservers(DrawableContainer.ALL_CHILD));
    }

    @Override
    protected void makeChange(ICanvas canvas, int nTh, int max) {
        canvas.clip(shape.get());
    }

    @Override
    protected void makeRestore(ICanvas canvas) {
        canvas.restore();
    }
}
