package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
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
public class Container extends ChangeableDrawable {
    private final ArrayList<IDrawable> list;
    private boolean xSplit = false;
    private double margin = 0; // < 1 -> percent, >=1 -> pixel
    private double spaceBetween = 0; // < 1 -> percent, >=1 -> pixel

    public Container(boolean xSplit, double margin, double spaceBetween) {
        this();
        this.xSplit = xSplit;
        this.margin = margin;
        this.spaceBetween = spaceBetween;
    }

    public Container(List<IDrawable> list) {
        this.list = new ArrayList<>(list);
        setRedraw();
    }

    public Container() {
        this.list = new ArrayList<>();
    }

    public boolean isxSplit() {
        return xSplit;
    }

    public Container setxSplit(boolean xSplit) {
        this.xSplit = xSplit;
        setRedraw();
        return this;
    }

    public double getMargin() {
        return margin;
    }

    public Container setMargin(double margin) {
        this.margin = margin;
        setRedraw();
        return this;
    }

    public double getSpaceBetween() {
        return spaceBetween;
    }

    public Container setSpaceBetween(double spaceBetween) {
        this.spaceBetween = spaceBetween;
        setRedraw();
        return this;
    }

    public Container add(IDrawable drawable) {
        list.add(drawable);
        setRedraw();
        return this;
    }

    public Container add(int elem, IDrawable drawable) {
        list.add(elem, drawable);
        setRedraw();
        return this;
    }

    public Container remove(IDrawable drawable) {
        list.remove(drawable);
        setRedraw();
        return this;
    }

    public Container remove(int elem) {
        list.remove(elem);
        setRedraw();
        return this;
    }

    public Container set(int elem, IDrawable drawable) {
        list.set(elem, drawable);
        setRedraw();
        return this;
    }

    @Override
    public boolean needRedraw() {
        boolean childNeedRedraw = false;
        for (IDrawable drawable : list) {
            childNeedRedraw |= drawable.needRedraw();
        }

        return super.needRedraw() || childNeedRedraw;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (list.isEmpty()) {
            return;
        }

        Rectangle rectangle = canvas.getBoundingRectangle();

        if (margin >= 1) {
            rectangle = new Rectangle(rectangle.getStart().add(margin),
                    rectangle.getEnd().add(-margin));
        } else {
            rectangle = new Rectangle(rectangle.getStart().add(rectangle.getSize().asPoint().multiple(margin / 2)),
                    rectangle.getStart().add(rectangle.getSize().asPoint().multiple(1 - margin / 2)));
        }

        Point dimension = xSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = xSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        Point elemSize = rectangle.getSize().asPoint().multiple(dimension.multiple(1.0 / list.size()).add(otherDimension));

        double space;
        if (spaceBetween >= 1) {
            space = spaceBetween;
        } else {
            space = elemSize.multiple(dimension).asSize().getLongerDimension() * spaceBetween;
        }

        for (int i = 0; i < list.size(); ++i) {
            Rectangle toDraw = new Rectangle(rectangle.getStart()
                    .add(elemSize.multiple(dimension.multiple(i)))
                    .add(dimension.multiple(space / 2.0)),
                    rectangle.getStart()
                            .add(elemSize.multiple(dimension.multiple(i + 1).add(otherDimension)))
                            .add(dimension.multiple(-space / 2.0)));
            canvas.clip(toDraw);
            if (list.get(i).needRedraw()) {
                setRedraw();
            }
            if (super.needRedraw()) {
                list.get(i).forceDraw(canvas);
            } else {
                list.get(i).draw(canvas);
            }
            canvas.restore();
        }
    }
}
