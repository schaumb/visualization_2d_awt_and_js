package bxlx.system.input.clickable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.container.Container;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ColorScheme;

import java.util.Arrays;

/**
 * Created by qqcs on 2017.02.07..
 */
public class CheckboxClickable extends OnOffClickable {
    private final Rect rect;
    private final AspectRatioDrawable<?> container;

    public CheckboxClickable() {
        rect = new Rect();
        container = new AspectRatioDrawable<>(new Container<>(Arrays.asList(
                new ColoredDrawable<>(rect, () -> ColorScheme.getCurrentColorScheme().buttonBorderColor),
                new ColoredDrawable<>(
                        new MarginDrawable<>(
                                rect, 3, 3
                        ), () -> disabled.get() ? ColorScheme.getCurrentColorScheme().disabledColor :
                            inside.get() ? ColorScheme.getCurrentColorScheme().insideColor : ColorScheme.getCurrentColorScheme().buttonColor
                ),
                new ColoredDrawable<>(
                        new Text(() -> getOn().get() ? "✔" : "✘"),
                        () -> ColorScheme.getCurrentColorScheme().buttonTextColor
                )
        )), false, -1, 0, 1.0);
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().orIf(true, container.needRedraw());
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        container.setRedraw();
        container.forceDraw(canvas);
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return rect.isContains(
                bound.getSize().getWidth() != bound.getSize().getLongerDimension() ? bound :
                        new AspectRatioDrawable<>(null, false, -1, 0, 1)
                                .getClip().get().apply(bound)
                , position);
    }
}
