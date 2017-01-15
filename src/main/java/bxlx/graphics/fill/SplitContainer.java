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
public class SplitContainer extends DrawableContainer<IDrawable> {
    private final ChangeableValue<Boolean> xSplit;

    public SplitContainer(boolean xSplit, List<IDrawable> list) {
        super(list);
        this.xSplit = new ChangeableValue<>(this, xSplit);
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

    public ChangeableValue<Boolean> getxSplit() {
        return xSplit;
    }

    @Override
    public void add(IDrawable drawable) {
        super.add(drawable);
    }

    @Override
    public ChangeableValue<IDrawable> get(int index) {
        return super.get(index);
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (size() == 0) {
            return;
        }

        boolean forcedRedraw = !needRedraw() || iChanged();

        Rectangle rectangle = canvas.getBoundingRectangle();

        boolean nowXSplit = xSplit.get();

        Point dimension = nowXSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = nowXSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        Point elemSize = rectangle.getSize().asPoint().multiple(dimension.multiple(1.0 / size()).add(otherDimension));

        for (int i = 0; i < size(); ++i) {
            IDrawable child = get(i).get();
            if (child == null) {
                continue;
            }

            Rectangle toDraw = new Rectangle(rectangle.getStart()
                    .add(elemSize.multiple(dimension.multiple(i))),
                    rectangle.getStart()
                            .add(elemSize.multiple(dimension.multiple(i + 1).add(otherDimension))));
            canvas.clip(toDraw);
            if (forcedRedraw || get(i).isChanged()) {
                child.forceDraw(canvas);
            } else {
                child.draw(canvas);
            }
            canvas.restore();
        }
    }
}
