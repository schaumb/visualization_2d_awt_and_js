package bxlx.graphics.drawable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.11..
 */
public class ClippedDrawable<T extends IDrawable> extends DrawableWrapper<T> {
    private final ChangeableDrawable.ChangeableValue<Boolean> fake;
    private final ChangeableDrawable.ChangeableValue<UnaryOperator<Rectangle>> clip;

    public ClippedDrawable(T drawable, boolean fake, UnaryOperator<Rectangle> clip) {
        super(drawable);
        this.fake = new ChangeableDrawable.ChangeableValue<>(this, fake);
        this.clip = new ChangeableDrawable.ChangeableValue<>(this, clip);
    }

    public ChangeableDrawable.ChangeableValue<Boolean> getFake() {
        return fake;
    }

    public ChangeableDrawable.ChangeableValue<UnaryOperator<Rectangle>> getClip() {
        return clip;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        boolean nowFake = fake.get();
        if (nowFake) {
            canvas.fakeClip(clip.get().apply(canvas.getBoundingRectangle()));
        } else {
            canvas.clip(clip.get().apply(canvas.getBoundingRectangle()));
        }

        super.forceRedraw(canvas);

        if (nowFake) {
            canvas.fakeRestore();
        } else {
            canvas.restore();
        }
    }
}
