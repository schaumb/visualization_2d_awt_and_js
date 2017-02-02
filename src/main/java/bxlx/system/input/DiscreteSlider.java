package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Point;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.18..
 */
public class DiscreteSlider extends Slider {
    private double realCachedNow;
    private final ChangeableDrawable.ChangeableValue<Integer> from;
    private final ChangeableValue<Integer> to;

    public DiscreteSlider(boolean xDraw, int from, int to, int now, Supplier<Boolean> disabled) {
        super(xDraw, (double) (now - from) / (to - 1 - from), disabled);
        this.from = new ChangeableValue<>(this, from);
        this.to = new ChangeableValue<>(this, to);
        realCachedNow = (double) (now - from) / (to - 1 - from);
    }

    @Override
    public void move(Point position) {
        double set = getNow().get();
        getNow().setElem(realCachedNow);
        super.move(position);
        if (getNow().isChanged()) {
            realCachedNow = getNow().get();
            double nowFrom = from.get();
            double nowTo = to.get() - 1;
            int rounded = (int) Math.round(realCachedNow * (nowTo - nowFrom) + nowFrom);
            getNow().setElem((rounded - nowFrom) / (nowTo - nowFrom));
        } else {
            getNow().setElem(set);
        }
    }

    public int getValue() {
        double now = getNow().get();
        double nowFrom = from.get();
        double nowTo = to.get() - 1;
        return (int) Math.round(now * (nowTo - nowFrom) + nowFrom);
    }

    public DiscreteSlider setValue(int value) {
        double nowFrom = from.get();
        double nowTo = to.get() - 1;
        getNow().setElem((value - nowFrom) / (nowTo - nowFrom));
        return this;
    }
}
