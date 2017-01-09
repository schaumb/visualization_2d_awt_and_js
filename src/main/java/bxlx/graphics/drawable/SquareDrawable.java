package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;


/**
 * Created by qqcs on 2017.01.09..
 */
public class SquareDrawable extends DrawableWrapper {
    private int alignX;
    private int alignY;
    private double lastDrewAlignX = -2;
    private double lastDrewAlignY = -2;

    public SquareDrawable(IDrawable wrapped) {
        this(wrapped, 0, 0);
    }

    public SquareDrawable(IDrawable wrapped, int alignX, int alignY) {
        super(wrapped);
        this.alignX = (int) Math.signum(alignX);
        this.alignY = (int) Math.signum(alignY);
    }

    public int getAlignX() {
        return alignX;
    }

    public SquareDrawable setAlignX(int alignX) {
        this.alignX = (int) Math.signum(alignX);
        return this;
    }

    public int getAlignY() {
        return alignY;
    }

    public SquareDrawable setAlignY(int alignY) {
        this.alignY = (int) Math.signum(alignY);
        return this;
    }

    @Override
    public boolean needRedraw() {
        return super.needRedraw() || alignX != lastDrewAlignX || alignY != lastDrewAlignY;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        Rectangle rectangle = canvas.getBoundingRectangle();

        boolean forcedRedraw = !needRedraw() || super.needRedraw();

        double widthAndHeight = rectangle.getSize().getShorterDimension();

        Point start = rectangle.getSize().asPoint().add(Point.same(-widthAndHeight))
                .multiple(new Point(alignX, alignY).add(1).multiple(1 / 2.0))
                .add(rectangle.getStart());
        canvas.clip(new Rectangle(start, Size.square(widthAndHeight)));

        if (forcedRedraw) {
            lastDrewAlignX = alignX;
            lastDrewAlignY = alignY;
            super.forceDraw(canvas);
        } else {
            getWrapped().draw(canvas);
        }

        canvas.restore();
    }
}
