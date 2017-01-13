package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.09..
 */
public class MarginDrawable extends ClippedDrawable {
    private double marginX; // < 1 -> percent, >=1 -> pixel
    private double marginY; // < 1 -> percent, >=1 -> pixel

    private static UnaryOperator<Rectangle> marginMake(double marginX, double marginY) {
        return rectangle -> {
            Point pixel = new Point(
                    marginX >= 1 ? marginX : marginX * rectangle.getSize().getWidth() / 2,
                    marginY >= 1 ? marginY : marginY * rectangle.getSize().getHeight() / 2
            );
            return new Rectangle(rectangle.getStart().add(pixel),
                    rectangle.getEnd().add(pixel.negate()));
        };
    }

    public MarginDrawable(IDrawable wrapped, double marginX, double marginY) {
        super(wrapped, marginMake(marginX, marginY));
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

    public double getMarginY() {
        return marginY;
    }

    public MarginDrawable setMarginX(double marginX) {
        this.marginX = marginX;
        setClip(marginMake(marginX, marginY));
        return this;
    }

    public MarginDrawable setMarginY(double marginY) {
        this.marginY = marginY;
        setClip(marginMake(marginX, marginY));
        return this;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        Rectangle rectangle = canvas.getBoundingRectangle();

        canvas.clip(rectangle);
        super.forceRedraw(canvas);
        canvas.restore();
    }
}
