package bxlx.system.input.clickable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Cursor;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.SystemSpecific;
import bxlx.system.input.Button;

/**
 * Created by qqcs on 2017.02.07..
 */
public class CursorChangeClickable extends Button.Clickable {
    private final ChangeableDrawable.ChangeableValue<Boolean> hoverChangeable;

    public CursorChangeClickable() {
        hoverChangeable = new ChangeableDrawable.ChangeableValue<>(this, () -> getHoverNow());
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return true;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        SystemSpecific.get().setCursor(hoverChangeable.get() ? Cursor.HAND : Cursor.DEFAULT);
    }
}
