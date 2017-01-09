package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.04..
 */
public class Splitter extends ChangeableDrawable {
    private boolean xSplit;
    private double separate;

    private IDrawable first;
    private IDrawable second;

    public Splitter(boolean xSplit, double separate, IDrawable first, IDrawable second) {
        this.xSplit = xSplit;
        this.separate = separate;
        this.first = first;
        this.second = second;
        setRedraw();
    }

    public Splitter(double separate, IDrawable first, IDrawable second) {
        this(false, separate, first, second);
    }

    public Splitter(IDrawable first, IDrawable second) {
        this(0.5, first, second);
    }

    public Splitter() {
        this(null, null);
    }

    public boolean isxSplit() {
        return xSplit;
    }

    public void setxSplit(boolean xSplit) {
        this.xSplit = xSplit;
        setRedraw();
    }

    public double getSeparate() {
        return separate;
    }

    public void setSeparate(double separate) {
        this.separate = separate;
        setRedraw();
    }

    public IDrawable getFirst() {
        return first;
    }

    public void setFirst(IDrawable first) {
        this.first = first;
        setRedraw();
    }

    public IDrawable getSecond() {
        return second;
    }

    public void setSecond(IDrawable second) {
        this.second = second;
        setRedraw();
    }

    @Override
    public boolean needRedraw() {
        return super.needRedraw() || first.needRedraw() || second.needRedraw();
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (first == null) {
            if (second == null) {
                return;
            }
            second.forceDraw(canvas);
            return;
        }
        if (second == null) {
            first.forceDraw(canvas);
            return;
        }

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
        if (super.needRedraw()) {
            first.forceDraw(canvas);
        } else {
            first.draw(canvas);
        }
        canvas.restore();

        canvas.clip(new Rectangle(
                rectangle.getStart().add(dimension.multiple(firstSize)),
                rectangle.getEnd()));

        if (super.needRedraw()) {
            second.forceDraw(canvas);
        } else {
            second.draw(canvas);
        }
        canvas.restore();
    }
}
