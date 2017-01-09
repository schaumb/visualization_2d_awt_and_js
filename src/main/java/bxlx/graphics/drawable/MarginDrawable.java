package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.09..
 */
public class MarginDrawable extends DrawableWrapper {
    private double marginX; // < 1 -> percent, >=1 -> pixel
    private double marginY; // < 1 -> percent, >=1 -> pixel
    private double lastDrewMarginX = -1;
    private double lastDrewMarginY = -1;

    public MarginDrawable(IDrawable wrapped, double marginX, double marginY) {
        super(wrapped);
        this.marginX = marginX;
        this.marginY = marginY;
    }

    public MarginDrawable(IDrawable wrapped, double margin) {
        this(wrapped, margin, margin);
    }

    public MarginDrawable(IDrawable wrapped) {
        this(wrapped, 0);
    }

    public double getMarginX() {
        return marginX;
    }

    public void setMarginX(double marginX) {
        this.marginX = marginX;
    }

    public double getMarginY() {
        return marginY;
    }

    public void setMarginY(double marginY) {
        this.marginY = marginY;
    }

    @Override
    public boolean needRedraw() {
        return super.needRedraw() ||
                marginX != lastDrewMarginX ||
                marginY != lastDrewMarginY;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        Rectangle rectangle = canvas.getBoundingRectangle();

        Point pixel = new Point(
                marginX >= 1 ? marginX : marginX * rectangle.getSize().getWidth() / 2,
                marginY >= 1 ? marginY : marginY * rectangle.getSize().getHeight() / 2
        );
        rectangle = new Rectangle(rectangle.getStart().add(pixel),
                rectangle.getEnd().add(pixel.negate()));

        canvas.clip(rectangle);

        if (marginX != lastDrewMarginX ||
                marginY != lastDrewMarginY || !super.needRedraw()) {
            lastDrewMarginX = marginX;
            lastDrewMarginY = marginY;
            super.forceDraw(canvas);
        } else {
            getWrapped().draw(canvas);
        }

        canvas.restore();
    }
}
