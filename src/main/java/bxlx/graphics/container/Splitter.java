package bxlx.graphics.container;

import bxlx.graphics.Drawable;
import bxlx.graphics.DrawableContainer;
import bxlx.graphics.drawable_helper.ChangerBuilder;
import bxlx.graphics.drawable_helper.ObservableRectangleTranslator;
import bxlx.system.ObservableValue;

import java.util.Arrays;

/**
 * Created by qqcs on 2017.01.04..
 */
public class Splitter extends DrawableContainer<Drawable> {
    private final ObservableValue<Boolean> xSplit;
    private final ObservableValue<Double> separate;

    public Splitter(boolean xSplit, double separate, Drawable first, Drawable second) {
        this(new ObservableValue<>(xSplit), new ObservableValue<>(separate), first, second);
    }

    public Splitter(ObservableValue<Boolean> xSplit, ObservableValue<Double> separate, Drawable first, Drawable second) {
        super(ChangerBuilder.ClipperBuilder.clip(ObservableRectangleTranslator.different(
            ObservableRectangleTranslator.marginObserve(
                    new ObservableValue<>(0.0),
                    new ObservableValue<>(() -> !xSplit.get() ? 1.0 : separate.get(), Arrays.asList(xSplit, separate)),
                    new ObservableValue<>(0.0),
                    new ObservableValue<>(() -> xSplit.get() ? 1.0 : separate.get(), Arrays.asList(xSplit, separate))),
                ObservableRectangleTranslator.marginObserve(
                        new ObservableValue<>(() -> !xSplit.get() ? 0.0 : separate.get(), Arrays.asList(xSplit, separate)),
                        new ObservableValue<>(1.0),
                        new ObservableValue<>(() -> xSplit.get() ? 0.0 : separate.get(), Arrays.asList(xSplit, separate)),
                        new ObservableValue<>(1.0)))).get());

        this.xSplit = xSplit;
        this.separate = separate;
        add(first);
        add(second);

        xSplit.addObserver((observable, from) -> this.setRedrawAllChild());
        separate.addObserver((observable, from) -> this.setRedrawAllChild());
    }

    public Splitter(boolean xSplit) {
        this(xSplit, 0.5, null, null);
    }

    public static Splitter threeWaySplit(boolean xSplit, double centerSeparate,
                                         Drawable first, Drawable center, Drawable last) {
        double separate1;
        double separate2;

        if (centerSeparate <= -1) {
            separate1 = Math.round(-centerSeparate / 2);
            separate2 = Math.round(centerSeparate / 2);
        } else if (centerSeparate < 0) {
            separate1 = -centerSeparate / 2;
            separate2 = centerSeparate / (2 + centerSeparate);
        } else if (centerSeparate < 1) {
            separate1 = (1 + centerSeparate) / -2;
            separate2 = 2 * centerSeparate / (1 + centerSeparate);
        } else {
            separate1 = -centerSeparate;
            separate2 = centerSeparate;
        }

        return new Splitter(xSplit, separate1, first,
                new Splitter(xSplit, separate2, center, last));

    }


    public ObservableValue<Boolean> getxSplit() {
        return xSplit;
    }

    public ObservableValue<Double> getSeparate() {
        return separate;
    }

    @Override
    protected void updateFromChild(Drawable drawable) {

    }
}
