package bxlx.graphics.fill;

import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.DrawableContainer;
import bxlx.graphics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2017.01.04..
 */
public class SplitContainer extends DrawableContainer {
    private boolean xSplit = false;

    public SplitContainer(boolean xSplit, List<IDrawable> list) {
        super(list);
        this.xSplit = xSplit;
    }

    public SplitContainer(boolean xSplit) {
        this(xSplit, new ArrayList<>());
    }

    public SplitContainer(List<IDrawable> list) {
        this(false, list);
    }

    public SplitContainer() {
        this(false, new ArrayList<>());
    }

    public boolean isxSplit() {
        return xSplit;
    }

    public SplitContainer setxSplit(boolean xSplit) {
        this.xSplit = xSplit;
        setRedraw();
        return this;
    }

    public SplitContainer add(IDrawable drawable) {
        children.add(drawable);
        setRedraw();
        return this;
    }

    public SplitContainer remove(IDrawable drawable) {
        children.remove(drawable);
        setRedraw();
        return this;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (children.isEmpty()) {
            return;
        }

        boolean forcedRedraw = !needRedraw() || iChanged();

        Rectangle rectangle = canvas.getBoundingRectangle();

        Point dimension = xSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = xSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        Point elemSize = rectangle.getSize().asPoint().multiple(dimension.multiple(1.0 / children.size()).add(otherDimension));

        for (int i = 0; i < children.size(); ++i) {
            if (children.get(i) == null) {
                continue;
            }

            Rectangle toDraw = new Rectangle(rectangle.getStart()
                    .add(elemSize.multiple(dimension.multiple(i))),
                    rectangle.getStart()
                            .add(elemSize.multiple(dimension.multiple(i + 1).add(otherDimension))));
            canvas.clip(toDraw);
            if (forcedRedraw) {
                children.get(i).forceDraw(canvas);
            } else {
                children.get(i).draw(canvas);
            }
            canvas.restore();
        }
    }
}
