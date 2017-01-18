package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.18..
 */
public class FakeClippedDrawable extends ClippedDrawable {
    public FakeClippedDrawable(IDrawable drawable, UnaryOperator<Rectangle> clip) {
        super(drawable, clip);
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        canvas.fakeClip(getClip().get().apply(canvas.getBoundingRectangle()));
        super.forceRedraw(canvas);
        canvas.fakeRestore();
    }
}
