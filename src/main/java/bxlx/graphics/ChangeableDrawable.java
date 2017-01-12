package bxlx.graphics;

/**
 * Created by qqcs on 2017.01.09..
 */
public abstract class ChangeableDrawable implements IDrawable {
    private boolean redraw = false;

    public void setRedraw() {
        redraw = true;
    }

    @Override
    public boolean needRedraw() {
        return redraw;
    }

    @Override
    public final void forceDraw(ICanvas canvas) {
        forceRedraw(canvas);
        redraw = false;
    }

    @Override
    public void setOnlyForceDraw() {
        redraw = false;
    }

    protected abstract void forceRedraw(ICanvas canvas);
}
