package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.11..
 */
public class ClippedDrawable extends DrawableWrapper {
    private final ChangeableValue<UnaryOperator<Rectangle>> clip;

    public ClippedDrawable(IDrawable drawable, UnaryOperator<Rectangle> clip) {
        super(drawable);
        this.clip = new ChangeableValue<>(this, clip);
    }

    public ChangeableValue<UnaryOperator<Rectangle>> getClip() {
        return clip;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        canvas.clip(clip.get().apply(canvas.getBoundingRectangle()));
        super.forceRedraw(canvas);
        canvas.restore();
    }
}
