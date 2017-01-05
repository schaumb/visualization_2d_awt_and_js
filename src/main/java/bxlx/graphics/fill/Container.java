package bxlx.graphics.fill;

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
public class Container extends ArrayList<IDrawable> implements IDrawable {
    private boolean xSplit = false;
    private double margin = 0; // < 1 -> percent, >=1 -> pixel
    private double spaceBetween = 0; // < 1 -> percent, >=1 -> pixel

    public Container(boolean xSplit, double margin, double spaceBetween) {
        this.xSplit = xSplit;
        this.margin = margin;
        this.spaceBetween = spaceBetween;
    }

    public Container(List<IDrawable> elements) {
        super(elements);
    }

    public Container() {

    }

    public boolean isxSplit() {
        return xSplit;
    }

    public Container setxSplit(boolean xSplit) {
        this.xSplit = xSplit;
        return this;
    }

    public double getMargin() {
        return margin;
    }

    public Container setMargin(double margin) {
        this.margin = margin;
        return this;
    }

    public double getSpaceBetween() {
        return spaceBetween;
    }

    public Container setSpaceBetween(double spaceBetween) {
        this.spaceBetween = spaceBetween;
        return this;
    }

    @Override
    public void draw(ICanvas canvas) {
        if(isEmpty()) {
            return;
        }

        Rectangle rectangle = canvas.getBoundingRectangle();

        if(margin >= 1) {
            rectangle = new Rectangle(rectangle.getStart().add(margin),
                    rectangle.getEnd().add(-margin));
        } else {
            rectangle = new Rectangle(rectangle.getStart().add(rectangle.getSize().asPoint().multiple(margin / 2)),
                    rectangle.getStart().add(rectangle.getSize().asPoint().multiple(1 - margin / 2)));
        }

        Point dimension = xSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = xSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        Point elemSize = rectangle.getSize().asPoint().multiple(dimension.multiple(1.0 / size()).add(otherDimension));

        double space;
        if(spaceBetween >= 1) {
            space = spaceBetween;
        } else {
            space = elemSize.multiple(dimension).asSize().getLongerDimension() * spaceBetween;
        }

        for(int i = 0; i < size(); ++i) {
            Rectangle toDraw = new Rectangle(rectangle.getStart()
                    .add(elemSize.multiple(dimension.multiple(i)))
                    .add(dimension.multiple(space / 2.0)),
                    rectangle.getStart()
                    .add(elemSize.multiple(dimension.multiple(i + 1).add(otherDimension)))
                    .add(dimension.multiple(-space / 2.0)));
            canvas.clip(toDraw);
            get(i).draw(canvas);
            canvas.restore();
        }
    }
}
