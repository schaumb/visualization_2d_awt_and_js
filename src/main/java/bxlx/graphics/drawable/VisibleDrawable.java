package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.09..
 */
public class VisibleDrawable<T extends VisibleDrawable.VisibleDraw> extends DrawableWrapper<T> {
    private final ChangeableValue<Boolean> visible;

    public VisibleDrawable(T wrapped, boolean visible) {
        super(wrapped);
        this.visible = new ChangeableValue<>(this, visible);
    }

    public VisibleDrawable(T wrapped, Supplier<Boolean> visible) {
        super(wrapped);
        this.visible = new ChangeableValue<>(this, visible);
    }

    public ChangeableValue<Boolean> getVisible() {
        return visible;
    }


    @Override
    public Redraw needRedraw() {
        return super.needRedraw().setIf(!visible.get() && visible.isChanged(), Redraw.PARENT_NEED_REDRAW);
    }

    @Override
    public Redraw childrenChanged() {
        return new Redraw().orIf(visible.get(), super.childrenChanged());
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (getChild().get() == null)
            return;

        Redraw redraw = needRedraw();
        boolean noNeedRedraw = redraw.noNeedRedraw();
        boolean iNeedRedraw = redraw.iNeedRedraw();

        if (visible.get()) {
            if (noNeedRedraw) {
                super.forceRedraw(canvas);
            } else if (iNeedRedraw) {
                getChild().get().setRedraw();
                super.forceRedraw(canvas);
            } else {
                getChild().get().draw(canvas);
            }
        } else {
            getChild().get().noVisibleDraw(canvas);
        }
    }

    public interface VisibleDraw extends IDrawable {
        void noVisibleDraw(ICanvas canvas);
    }
}
