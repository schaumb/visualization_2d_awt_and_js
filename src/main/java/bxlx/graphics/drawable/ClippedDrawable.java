package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.11..
 */
public class ClippedDrawable extends DrawableWrapper {
    private UnaryOperator<Rectangle> clip;

    public ClippedDrawable(IDrawable drawable, UnaryOperator<Rectangle> clip) {
        super(drawable);
        this.clip = clip;
    }

    public UnaryOperator<Rectangle> getClip() {
        return clip;
    }

    public ClippedDrawable setClip(UnaryOperator<Rectangle> clip) {
        this.clip = clip;
        setRedraw();
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        canvas.clip(clip.apply(canvas.getBoundingRectangle()));
        super.forceRedraw(canvas);
        canvas.restore();
    }
}
