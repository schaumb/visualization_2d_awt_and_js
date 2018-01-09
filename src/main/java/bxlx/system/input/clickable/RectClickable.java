package bxlx.system.input.clickable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.input.Button;

/**
 * Created by qqcs on 2017.02.07..
 */
public class RectClickable extends Button.Clickable {
    private final Rect rect = new Rect();
    private final IDrawable drawable;

    private IDrawable transformSign(IDrawable drawable) {
        if (drawable == null) return null;
        return new ClippedDrawable<>(new ColoredDrawable<>(drawable, () -> disabled.get() ? Color.WHITE : Color.BLACK), false,
                rectangle -> new Rectangle(
                        rectangle.getStart().add(rectangle.getSize().asPoint().multiple(1 / 16.0)),
                        rectangle.getStart().add(rectangle.getSize().asPoint().multiple(15 / 16.0))
                ));
    }

    public RectClickable(IDrawable drawable) {
        this.drawable = transformSign(drawable);
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return drawable == null ? new IDrawable.Redraw() : drawable.needRedraw();
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        canvas.setColor(disabled.get() ? Color.LIGHT_GRAY : inside.get() ? Color.DARK_GRAY : Color.GRAY);
        rect.forceDraw(canvas);
        if (drawable != null) {
            drawable.forceDraw(canvas);
        }
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return rect.isContains(bound, position);
    }
}

