package bxlx.graphics.container;

import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2017.01.04..
 */
public class SplitContainer<T extends IDrawable> extends SizeChangeableContainer<T, SplitContainer<T>> {
    private final ChangeableValue<Boolean> xSplit;

    public SplitContainer() {
        this(false, new ArrayList<>());
    }

    public SplitContainer(boolean xSplit) {
        this(xSplit, new ArrayList<>());
    }

    public SplitContainer(List<T> list) {
        this(false, list);
    }

    @Override
    public SplitContainer<T> getThis() {
        return this;
    }

    public SplitContainer(boolean xSplit, List<T> list) {
        super(list);
        this.xSplit = new ChangeableValue<>(this, xSplit);
    }

    public ChangeableValue<Boolean> getxSplit() {
        return xSplit;
    }

    @Override
    protected boolean parentRedrawSatisfy() {
        return false;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (size() == 0) {
            return;
        }

        Redraw redraw = needRedraw();
        boolean noNeedRedraw = redraw.noNeedRedraw();
        boolean iNeedRedraw = redraw.iNeedRedraw();

        Rectangle rectangle = canvas.getBoundingRectangle();

        boolean nowXSplit = xSplit.get();

        Point dimension = nowXSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = nowXSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        Point elemSize = rectangle.getSize().asPoint().multiple(dimension.multiple(1.0 / size()).add(otherDimension));

        for (int i = 0; i < size(); ++i) {
            T child = get(i).get();
            if (child == null) {
                continue;
            }

            Rectangle toDraw = new Rectangle(rectangle.getStart()
                    .add(elemSize.multiple(dimension.multiple(i))),
                    rectangle.getStart()
                            .add(elemSize.multiple(dimension.multiple(i + 1).add(otherDimension))));
            canvas.clip(toDraw);
            if (noNeedRedraw) {
                child.forceDraw(canvas);
            } else if (iNeedRedraw) {
                child.setRedraw();
                child.forceDraw(canvas);
            } else {
                child.draw(canvas);
            }
            canvas.restore();
        }
    }
}
