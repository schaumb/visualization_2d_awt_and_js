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
public class Splitter extends DrawableContainer {
    private boolean xSplit;
    private double separate;

    public Splitter(boolean xSplit, double separate, IDrawable first, IDrawable second) {
        super(Arrays.asList(first, second));
        this.xSplit = xSplit;
        this.separate = separate;
        setRedraw();
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

    public boolean isxSplit() {
        return xSplit;
    }

    public Splitter setxSplit(boolean xSplit) {
        this.xSplit = xSplit;
        setRedraw();
        return this;
    }

    public double getSeparate() {
        return separate;
    }

    public Splitter setSeparate(double separate) {
        this.separate = separate;
        setRedraw();
        return this;
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
        if (getFirst() == null) {
            if (getSecond() == null) {
                return;
            }
            getSecond().forceDraw(canvas);
            return;
        }
        if (getSecond() == null) {
            getFirst().forceDraw(canvas);
            return;
        }

        boolean forcedRedraw = !needRedraw() || iChanged();

        Rectangle rectangle = canvas.getBoundingRectangle();

        Point dimension = xSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = xSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        double firstSize = rectangle.getSize().asPoint().multiple(dimension).asSize().getLongerDimension();
        if (separate <= -1) {
            firstSize = Math.max(0, firstSize + separate);
        } else if (separate < 0) {
            firstSize *= 1 + separate;
        } else if (separate < 1) {
            firstSize *= separate;
        } else {
            firstSize = Math.min(firstSize, separate);
        }

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
