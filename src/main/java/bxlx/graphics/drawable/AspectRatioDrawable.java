package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Supplier;


/**
 * Created by qqcs on 2017.01.09..
 */
public class AspectRatioDrawable extends DrawableWrapper {
    private int alignX;
    private int alignY;
    private Supplier<Double> ratio;

    private int lastDrewAlignX = -2;
    private int lastDrewAlignY = -2;
    private double lastDrewRatio = -1;

    public AspectRatioDrawable(IDrawable wrapped) {
        this(wrapped, () -> 1.0);
    }

    public AspectRatioDrawable(IDrawable wrapped, Supplier<Double> ratio) {
        this(wrapped, 0, 0, ratio);
    }

    public AspectRatioDrawable(IDrawable wrapped, int alignX, int alignY, Supplier<Double> ratio) {
        super(wrapped);
        this.alignX = (int) Math.signum(alignX);
        this.alignY = (int) Math.signum(alignY);
        this.ratio = ratio;
    }

    public int getAlignX() {
        return alignX;
    }

    public AspectRatioDrawable setAlignX(int alignX) {
        this.alignX = (int) Math.signum(alignX);
        return this;
    }

    public int getAlignY() {
        return alignY;
    }

    public AspectRatioDrawable setAlignY(int alignY) {
        this.alignY = (int) Math.signum(alignY);
        return this;
    }

    public Supplier<Double> getRatio() {
        return ratio;
    }

    public AspectRatioDrawable setRatio(Supplier<Double> ratio) {
        this.ratio = ratio;
        return this;
    }

    @Override
    public boolean needRedraw() {
        return super.needRedraw() || alignX != lastDrewAlignX || alignY != lastDrewAlignY || ratio.get() != lastDrewRatio;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        Rectangle rectangle = canvas.getBoundingRectangle();

        double nowRatio = ratio.get();
        if(nowRatio <= 0) {
            nowRatio = 1;
        }

        boolean forcedRedraw = !needRedraw() || alignX != lastDrewAlignX || alignY != lastDrewAlignY || nowRatio != lastDrewRatio;

        double width = Math.min(rectangle.getSize().getWidth(), rectangle.getSize().getHeight() / nowRatio);
        Size size = new Size(width, width * nowRatio);

        Point start = rectangle.getSize().asPoint().add(size.asPoint().negate())
                .multiple(new Point(alignX, alignY).add(1).multiple(1 / 2.0))
                .add(rectangle.getStart());
        canvas.clip(new Rectangle(start, size));

        if (forcedRedraw) {
            lastDrewAlignX = alignX;
            lastDrewAlignY = alignY;
            lastDrewRatio = nowRatio;
            getWrapped().forceDraw(canvas);
        } else {
            getWrapped().draw(canvas);
        }

        canvas.restore();
    }
}
