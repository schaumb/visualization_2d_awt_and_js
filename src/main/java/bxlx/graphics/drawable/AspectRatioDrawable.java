package bxlx.graphics.drawable;

import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Supplier;


/**
 * Created by qqcs on 2017.01.09..
 */
public class AspectRatioDrawable<T extends IDrawable> extends ClippedDrawable<T> {
    private final ChangeableValue<Integer> alignX;
    private final ChangeableValue<Integer> alignY;
    private final ChangeableValue<Double> ratio;

    public AspectRatioDrawable(T wrapped, boolean fake, int alignX, int alignY, double ratio) {
        super(wrapped, fake, null);
        this.alignX = new ChangeableValue<>(this, alignX);
        this.alignY = new ChangeableValue<>(this, alignY);
        this.ratio = new ChangeableValue<>(this, ratio);
        setTheClip();
    }

    public AspectRatioDrawable(T wrapped, boolean fake, int alignX, int alignY, Supplier<Double> ratio) {
        super(wrapped, fake, null);
        this.alignX = new ChangeableValue<>(this, alignX);
        this.alignY = new ChangeableValue<>(this, alignY);
        this.ratio = new ChangeableValue<>(this, ratio);
        setTheClip();
    }

    public ChangeableValue<Integer> getAlignX() {
        return alignX;
    }

    public ChangeableValue<Integer> getAlignY() {
        return alignY;
    }

    public ChangeableValue<Double> getRatio() {
        return ratio;
    }

    private void setTheClip() {
        getClip().setElem(rectangle -> {
            double nowRatio = ratio.get();
            if (nowRatio <= 0) {
                nowRatio = 1;
            }

            int nowAlignX = (int) Math.signum(alignX.get());
            int nowAlignY = (int) Math.signum(alignY.get());


            double width = Math.min(rectangle.getSize().getWidth(), rectangle.getSize().getHeight() / nowRatio);
            Size size = new Size(width, width * nowRatio);

            Point start = rectangle.getSize().asPoint().add(size.asPoint().negate())
                    .multiple(new Point(nowAlignX, nowAlignY).add(1).multiple(1 / 2.0))
                    .add(rectangle.getStart());
            return new Rectangle(start, size);
        });
    }
}
