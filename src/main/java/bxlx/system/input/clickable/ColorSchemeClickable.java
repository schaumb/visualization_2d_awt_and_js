package bxlx.system.input.clickable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ColorScheme;

import java.util.function.Function;

/**
 * Created by qqcs on 2017.02.07..
 */
public class ColorSchemeClickable extends OnOffClickable {
    private final boolean clickedNeed;
    private final Rect rect = new Rect();
    private final MarginDrawable<Rect> smallerRect;

    public ColorSchemeClickable(boolean clickedNeed) {
        this(clickedNeed, r -> new MarginDrawable<>(r, 10, 10));
    }

    public ColorSchemeClickable(boolean clickedNeed, Function<Rect, MarginDrawable<Rect>> smallerRect) {
        this.clickedNeed = clickedNeed;
        this.smallerRect = smallerRect.apply(rect);
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        ColorScheme colors = ColorScheme.getCurrentColorScheme();
        super.forceRedraw(canvas);
        canvas.setColor(colors.buttonBorderColor);

        rect.forceDraw(canvas);
        canvas.setColor(disabled.get() ? colors.disabledColor : inside.get() ? colors.insideColor : clickedNeed && getOn().get() ? colors.clickedColor : colors.buttonColor);
        smallerRect.forceDraw(canvas);
        canvas.setColor(colors.buttonTextColor);
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return rect.isContains(bound, position);
    }
}