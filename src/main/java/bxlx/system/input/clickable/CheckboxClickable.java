package bxlx.system.input.clickable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;

import java.util.Arrays;

/**
 * Created by qqcs on 2017.02.07..
 */
public class CheckboxClickable extends OnOffClickable {
    private final Rect rect;
    private final Splitter container;

    public CheckboxClickable(IDrawable other) {
        rect = new Rect();
        container = new Splitter(true, 0,
                new Container(Arrays.asList(
                        new ColoredDrawable<>(rect, Color.BLACK),
                        new ColoredDrawable<>(
                                new MarginDrawable<>(
                                        rect, 0.1, 0.1
                                ), () -> disabled.get() ? Color.LIGHT_GRAY : inside.get() ? Color.DARK_GRAY : Color.GRAY
                        ),
                        new ColoredDrawable<>(
                                new Text(() -> getOn().get() ? "✔" : "✘"),
                                () -> disabled.get() ? Color.WHITE : Color.BLACK
                        )
                ))
                , other);
        container.getSeparate()
                .setElem(r -> r.getSize().getHeight());
    }

    @Override
    public Redraw needRedraw() {
        return super.needRedraw().orIf(true, container.needRedraw());
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        container.setRedraw();
        container.forceDraw(canvas);
        super.forceDraw(canvas);
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return rect.isContains(
                bound.getSize().getWidth() != bound.getSize().getLongerDimension() ? bound :
                        new AspectRatioDrawable<>(null, false, -1, -1, 1)
                                .getClip().get().apply(bound)
                , position);
    }
}
