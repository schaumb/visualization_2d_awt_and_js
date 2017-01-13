package bxlx.graphics.drawable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;


/**
 * Created by qqcs on 2017.01.09..
 */
public class AspectRatioDrawable extends ClippedDrawable {
    private boolean onlyForceRedraw;
    private int alignX;
    private int alignY;
    private Supplier<Double> ratio;

    private double lastDrewRatio = -1;

    private static UnaryOperator<Rectangle> ratioMake(Supplier<Double> ratio, int alignX, int alignY) {
        return rectangle -> {
            double nowRatio = ratio.get();
            if (nowRatio <= 0) {
                nowRatio = 1;
            }

            double width = Math.min(rectangle.getSize().getWidth(), rectangle.getSize().getHeight() / nowRatio);
            Size size = new Size(width, width * nowRatio);

            Point start = rectangle.getSize().asPoint().add(size.asPoint().negate())
                    .multiple(new Point(alignX, alignY).add(1).multiple(1 / 2.0))
                    .add(rectangle.getStart());
            return new Rectangle(start, size);
        };
    }

    public AspectRatioDrawable(IDrawable wrapped) {
        this(wrapped, () -> 1.0);
    }

    public AspectRatioDrawable(IDrawable wrapped, Supplier<Double> ratio) {
        this(wrapped, 0, 0, ratio);
    }

    public AspectRatioDrawable(IDrawable wrapped, int alignX, int alignY, Supplier<Double> ratio) {
        super(wrapped, ratioMake(ratio, alignX, alignY));
        this.alignX = (int) Math.signum(alignX);
        this.alignY = (int) Math.signum(alignY);
        this.ratio = ratio;
    }

    public int getAlignX() {
        return alignX;
    }

    public AspectRatioDrawable setAlignX(int alignX) {
        this.alignX = (int) Math.signum(alignX);
        setClip(ratioMake(ratio, alignX, alignY));
        return this;
    }

    public int getAlignY() {
        return alignY;
    }

    public AspectRatioDrawable setAlignY(int alignY) {
        this.alignY = (int) Math.signum(alignY);
        setClip(ratioMake(ratio, alignX, alignY));
        return this;
    }

    public Supplier<Double> getRatio() {
        return ratio;
    }

    public AspectRatioDrawable setRatio(Supplier<Double> ratio) {
        this.ratio = ratio;
        setClip(ratioMake(ratio, alignX, alignY));
        return this;
    }

    @Override
    public boolean needRedraw() {
        return !onlyForceRedraw && (super.needRedraw() || ratio.get() != lastDrewRatio);
    }

    @Override
    public void setOnlyForceDraw() {
        super.setOnlyForceDraw();
        onlyForceRedraw = true;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        onlyForceRedraw = false;

        super.forceRedraw(canvas);
        lastDrewRatio = ratio.get();
    }
}
