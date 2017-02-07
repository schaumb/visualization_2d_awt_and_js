package bxlx.system.input.clickable;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.02.07..
 */
public class SameImageClickable extends OnOffClickable {
    private final DrawImage img;
    private final UnaryOperator<Rectangle> normalClip;
    private final UnaryOperator<Rectangle> insideClip;
    private final UnaryOperator<Rectangle> disableClip;
    private final UnaryOperator<Rectangle> clickedClip;
    private Rectangle lastRectangle;
    private final Predicate<Color> clickable;

    public SameImageClickable(String src, boolean xSplit, boolean hasClicked, Predicate<Color> clickable) {
        this(src,
                xSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector(),
                xSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector(), hasClicked ? 4.0 : 3.0, clickable);
    }

    private SameImageClickable(String src, Point split, Point notSplit, double part, Predicate<Color> clickable) {
        this(src,
                r -> r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize()),
                r -> r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize())
                        .withStart(r.getStart().add(r.getSize().asPoint().multiple(split.inverse()))),
                r -> r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize())
                        .withStart(r.getStart().add(r.getSize().asPoint().multiple(split.inverse().multiple(2)))),
                r -> {
                    Rectangle res = r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize());
                    return part == 4.0 ? res.withStart(r.getStart().add(r.getSize().asPoint().multiple(split.inverse().multiple(3)))) : res;
                }, clickable);
    }

    public SameImageClickable(String src, UnaryOperator<Rectangle> normalClip, UnaryOperator<Rectangle> insideClip, UnaryOperator<Rectangle> disableClip, Predicate<Color> clickable) {
        this(src, normalClip, insideClip, disableClip, normalClip, clickable);
    }

    public SameImageClickable(String src, UnaryOperator<Rectangle> normalClip, UnaryOperator<Rectangle> insideClip, UnaryOperator<Rectangle> disableClip, UnaryOperator<Rectangle> clickedClip, Predicate<Color> clickable) {
        this.img = new DrawImage(src);
        this.normalClip = normalClip;
        this.insideClip = insideClip;
        this.disableClip = disableClip;
        this.clickedClip = clickedClip;
        this.clickable = clickable;
    }

    private UnaryOperator<Rectangle> getActualClip() {
        if (disabled.get()) {
            return disableClip;
        } else if (inside.get()) {
            return insideClip;
        } else if (getOn().get()) {
            return clickedClip;
        } else {
            return normalClip;
        }
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        lastRectangle = getActualClip().apply(canvas.getBoundingRectangle());

        canvas.fakeClip(lastRectangle);
        img.forceDraw(canvas);
        canvas.fakeRestore();
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return lastRectangle != null && clickable.test(img.getColor(lastRectangle, position));
    }
}
