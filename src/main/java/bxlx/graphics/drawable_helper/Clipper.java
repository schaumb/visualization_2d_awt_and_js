package bxlx.graphics.drawable_helper;

import bxlx.graphics.ICanvas;
import bxlx.graphics.shapes.Rectangle;

public class Clipper extends CanvasChanger {
    private boolean isFake;
    private RectangleTranslator rectangleTranslator;

    Clipper(boolean isFake, RectangleTranslator rectangleTranslator, CanvasChanger then) {
        super(then);
        this.isFake = isFake;
        this.rectangleTranslator = rectangleTranslator;

        if (rectangleTranslator instanceof ObservableRectangleTranslator) {
            ((ObservableRectangleTranslator) rectangleTranslator).addObserver(this);
        }
    }

    @Override
    public void makeChange(ICanvas canvas, int nTh, int max) {
        Rectangle rectangle = rectangleTranslator.getTranslatedRectangle(canvas.getBoundingRectangle(), nTh, max);

        if (isFake) {
            canvas.fakeClip(rectangle);
        } else {
            canvas.clip(rectangle);
        }
    }

    @Override
    public void makeRestore(ICanvas canvas) {
        if (isFake) {
            canvas.fakeRestore();
        } else {
            canvas.restore();
        }
    }
}
