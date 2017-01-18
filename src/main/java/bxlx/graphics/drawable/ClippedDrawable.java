package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.11..
 */
public class ClippedDrawable<T extends IDrawable> extends DrawableWrapper<T> {
    private final ChangeableValue<Boolean> fake;
    private final ChangeableValue<UnaryOperator<Rectangle>> clip;

    public ClippedDrawable(T drawable, boolean fake, UnaryOperator<Rectangle> clip) {
        super(drawable);
        this.fake = new ChangeableValue<>(this, fake);
        this.clip = new ChangeableValue<>(this, clip);
    }

    public ChangeableValue<Boolean> getFake() {
        return fake;
    }

    public ChangeableValue<UnaryOperator<Rectangle>> getClip() {
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
