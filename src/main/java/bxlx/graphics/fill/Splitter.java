package bxlx.graphics.fill;

import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.DrawableContainer;
import bxlx.graphics.shapes.Rectangle;

import java.util.Arrays;

/**
 * Created by qqcs on 2017.01.04..
 */
public class Splitter extends DrawableContainer<IDrawable> {
    private final ChangeableValue<Boolean> xSplit;
    private final ChangeableValue<Double> separate;

    public Splitter(boolean xSplit, double separate, IDrawable first, IDrawable second) {
        super(Arrays.asList(first, second));
        this.xSplit = new ChangeableValue<>(this, xSplit);
        this.separate = new ChangeableValue<>(this, separate);
    }

    public Splitter(double separate, IDrawable first, IDrawable second) {
        this(false, separate, first, second);
    }

    public Splitter(IDrawable first, IDrawable second) {
        this(false, 0.5, first, second);
    }

    public Splitter() {
        this(false, 0.5, null, null);
    }

    public Splitter(boolean xSplit) {
        this(xSplit, 0.5, null, null);
    }

    public static Splitter threeWaySplit(boolean xSplit, double centerSeparate,
                                         IDrawable first, IDrawable center, IDrawable last) {
        double separate1;
        double separate2;

        if (centerSeparate <= -1) {
            separate1 = Math.round(-centerSeparate / 2);
            separate2 = Math.round(centerSeparate / 2);
        } else if (centerSeparate < 0) {
            separate1 = -centerSeparate / 2;
            separate2 = centerSeparate / (2 + centerSeparate);
        } else if (centerSeparate < 1) {
            separate1 = (1 + centerSeparate) / -2;
            separate2 = 2 * centerSeparate / (1 + centerSeparate);
        } else {
            separate1 = -centerSeparate;
            separate2 = centerSeparate;
        }

        return new Splitter(xSplit, separate1, first,
                new Splitter(xSplit, separate2, center, last));

    }

    public ChangeableValue<Boolean> getxSplit() {
        return xSplit;
    }

    public ChangeableValue<Double> getSeparate() {
        return separate;
    }

    public IDrawable getFirst() {
        return children.get(0);
    }

    public Splitter setFirst(IDrawable first) {
        children.set(0, first);
        setRedraw();
        return this;
    }

    public IDrawable getSecond() {
        return children.get(1);
    }

    public Splitter setSecond(IDrawable second) {
        children.set(1, second);
        setRedraw();
        return this;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        boolean forcedRedraw = !needRedraw() || iChanged();

        Rectangle rectangle = canvas.getBoundingRectangle();

        boolean nowXSplit = xSplit.get();
        double nowSeparate = separate.get();

        Point dimension = nowXSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = nowXSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        double firstSize = rectangle.getSize().asPoint().multiple(dimension).asSize().getLongerDimension();
        if (nowSeparate <= -1) {
            firstSize = Math.max(0, firstSize + nowSeparate);
        } else if (nowSeparate <= 0) {
            firstSize *= 1 + nowSeparate;
        } else if (nowSeparate < 1) {
            firstSize *= nowSeparate;
        } else {
            firstSize = Math.min(firstSize, nowSeparate);
        }

        if (getFirst() != null) {
            canvas.clip(new Rectangle(
                    rectangle.getStart(),
                    rectangle.getStart().add(dimension.multiple(firstSize))
                            .add(otherDimension.multiple(rectangle.getSize().asPoint()))));
            if (forcedRedraw) {
                getFirst().forceDraw(canvas);
            } else {
                getFirst().draw(canvas);
            }
            canvas.restore();
        }

        if (getSecond() != null) {
            canvas.clip(new Rectangle(
                    rectangle.getStart().add(dimension.multiple(firstSize)),
                    rectangle.getEnd()));

            if (forcedRedraw) {
                getSecond().forceDraw(canvas);
            } else {
                getSecond().draw(canvas);
            }
            canvas.restore();
        }
    }
}
