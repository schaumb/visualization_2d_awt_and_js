package bxlx.graphics.drawable_helper;

import bxlx.graphics.DrawableContainer;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.Observable;
import bxlx.system.ObservableValue;
import bxlx.system.Observer;

import java.util.Collections;
import java.util.List;

public abstract class ObservableRectangleTranslator extends Observable<List<Integer>> implements RectangleTranslator {
    public static ObservableRectangleTranslator scaledObserve(ObservableValue<Double> scale) {
        ObservableRectangleTranslator result = new ObservableRectangleTranslator() {
            @Override
            public Rectangle getTranslatedRectangle(Rectangle original, int nTh, int max) {
                return original.getScaled(scale.get());
            }
        };

        scale.addObserver((observable, from) -> result.notifyObservers(DrawableContainer.ALL_CHILD));

        return result;
    }

    public static ObservableRectangleTranslator translatedObserve(ObservableValue<Point> translate) {
        ObservableRectangleTranslator result = new ObservableRectangleTranslator() {
            @Override
            public Rectangle getTranslatedRectangle(Rectangle original, int nTh, int max) {
                return original.getTranslated(translate.get());
            }
        };

        translate.addObserver((observable, from) -> result.notifyObservers(DrawableContainer.ALL_CHILD));

        return result;
    }

    public static ObservableRectangleTranslator aspectRatioObserve(ObservableValue<Double> nowRatio,
                                                                   ObservableValue<Double> alignX,
                                                                   ObservableValue<Double> alignY) {
        ObservableRectangleTranslator result = new ObservableRectangleTranslator() {
            @Override
            public Rectangle getTranslatedRectangle(Rectangle original, int nTh, int max) {
                return RectangleTranslator.aspectRatio(nowRatio.get(), alignX.get(), alignY.get())
                        .getTranslatedRectangle(original, nTh, max);
            }
        };

        Observer<Double> observer = (observable, from) -> result.notifyObservers(DrawableContainer.ALL_CHILD);
        nowRatio.addObserver(observer);
        alignX.addObserver(observer);
        alignY.addObserver(observer);

        return result;
    }

    public static ObservableRectangleTranslator marginObserve(ObservableValue<Double> all) {
        return marginObserve(all, all);
    }

    public static ObservableRectangleTranslator marginObserve(ObservableValue<Double> x, ObservableValue<Double> y) {
        return marginObserve(x, x, y, y);
    }

    public static ObservableRectangleTranslator marginObserve(ObservableValue<Double> left,
                                                              ObservableValue<Double> right,
                                                              ObservableValue<Double> top,
                                                              ObservableValue<Double> bottom) {
        ObservableRectangleTranslator result = new ObservableRectangleTranslator() {
            @Override
            public Rectangle getTranslatedRectangle(Rectangle original, int nTh, int max) {
                return RectangleTranslator.margin(left.get(), right.get(), top.get(), bottom.get())
                        .getTranslatedRectangle(original, nTh, max);
            }
        };

        Observer<Double> observer = (observable, from) -> result.notifyObservers(DrawableContainer.ALL_CHILD);
        left.addObserver(observer);
        right.addObserver(observer);
        top.addObserver(observer);
        bottom.addObserver(observer);

        return result;
    }

    public static ObservableRectangleTranslator visibility(ObservableValue<Boolean> visible) {
        ObservableRectangleTranslator result = new ObservableRectangleTranslator() {
            @Override
            public Rectangle getTranslatedRectangle(Rectangle original, int nTh, int max) {
                Boolean isVisible = visible.get();
                return isVisible != null && isVisible ? original : Rectangle.NULL_RECTANGLE;
            }
        };

        visible.addObserver((observable, from) -> result.notifyObservers(DrawableContainer.ALL_CHILD));

        return result;
    }

    public static ObservableRectangleTranslator different(RectangleTranslator... translators) {
        ObservableRectangleTranslator result = new ObservableRectangleTranslator() {
            @Override
            public Rectangle getTranslatedRectangle(Rectangle original, int nTh, int max) {
                return translators[nTh % translators.length].getTranslatedRectangle(original, nTh, max);
            }
        };

        for (int i = 0; i < translators.length; ++i) {
            RectangleTranslator translator = translators[i];
            if (translator instanceof ObservableRectangleTranslator) {
                int finalI = i;
                ((ObservableRectangleTranslator) translator).addObserver((observable, from) ->
                        result.notifyObservers(Collections.singletonList(finalI)));
            }
        }

        return result;
    }
}
