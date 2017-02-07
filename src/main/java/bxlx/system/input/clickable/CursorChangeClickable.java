package bxlx.system.input.clickable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Cursor;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.SystemSpecific;
import bxlx.system.input.Button;

/**
 * Created by qqcs on 2017.02.07..
 */
public class CursorChangeClickable extends Button.Clickable {
    private final IDrawable drawable;
    private final ChangeableDrawable.ChangeableValue<Boolean> hoverChangeable;

    public CursorChangeClickable(IDrawable drawable) {
        this.drawable = drawable;
        hoverChangeable = new ChangeableValue<>(this, () -> getHoverNow());
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return true;
    }

    @Override
    public Redraw needRedraw() {
        return super.needRedraw().orIf(true, drawable.needRedraw());
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        drawable.forceDraw(canvas);

        SystemSpecific.get().setCursor(hoverChangeable.get() ? Cursor.HAND : Cursor.DEFAULT);
    }
}
