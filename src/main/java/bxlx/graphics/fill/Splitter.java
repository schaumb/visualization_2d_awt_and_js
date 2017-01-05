package bxlx.graphics.fill;

import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.04..
 */
public class Splitter implements IDrawable {
    private boolean xSplit = false;
    private double separate = 0.5;

    private IDrawable first;
    private IDrawable second;

    public Splitter(boolean xSplit, double separate, IDrawable first, IDrawable second) {
        this.xSplit = xSplit;
        this.separate = separate;
        this.first = first;
        this.second = second;
    }

    public Splitter(double separate, IDrawable first, IDrawable second) {
        this.separate = separate;
        this.first = first;
        this.second = second;
    }

    public Splitter(IDrawable first, IDrawable second) {
        this.first = first;
        this.second = second;
    }

    public Splitter() {
    }

    public boolean isxSplit() {
        return xSplit;
    }

    public void setxSplit(boolean xSplit) {
        this.xSplit = xSplit;
    }

    public double getSeparate() {
        return separate;
    }

    public void setSeparate(double separate) {
        this.separate = separate;
    }

    public IDrawable getFirst() {
        return first;
    }

    public void setFirst(IDrawable first) {
        this.first = first;
    }

    public IDrawable getSecond() {
        return second;
    }

    public void setSecond(IDrawable second) {
        this.second = second;
    }

    @Override
    public void draw(ICanvas canvas) {
        if(first == null) {
            if(second == null) {
                return;
            }
            second.draw(canvas);
            return;
        }
        if(second == null) {
            first.draw(canvas);
            return;
        }

        Rectangle rectangle = canvas.getBoundingRectangle();

        Point dimension = xSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = xSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        double firstSize = rectangle.getSize().asPoint().multiple(dimension).asSize().getLongerDimension();
        if(separate <= -1) {
            firstSize = Math.max(0, firstSize + separate);
        } else if(separate < 0) {
            firstSize *= 1+separate;
        } else if(separate < 1) {
            firstSize *= separate;
        } else {
            firstSize = Math.min(firstSize, separate);
        }

        canvas.clip(new Rectangle(
                rectangle.getStart(),
                rectangle.getStart().add(dimension.multiple(firstSize))
                        .add(otherDimension.multiple(rectangle.getSize().asPoint()))));
        first.draw(canvas);
        canvas.restore();

        canvas.clip(new Rectangle(
                rectangle.getStart().add(dimension.multiple(firstSize)),
                rectangle.getEnd()));

        second.draw(canvas);
        canvas.restore();
    }
}
