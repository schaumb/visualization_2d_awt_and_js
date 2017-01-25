package bxlx.graphics.fill;

import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.DrawableContainer;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ValueOrSupplier;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.04..
 */
public class Splitter extends DrawableContainer<IDrawable> {
    private final ChangeableValue<Boolean> xSplit;
    private final ChangeableValue<Function<Rectangle, Double>> separate;

    public Splitter(boolean xSplit, double separate, IDrawable first, IDrawable second) {
        super(Arrays.asList(first, second));
        this.xSplit = new ChangeableValue<>(this, xSplit);
        this.separate = new ChangeableValue<>(this, r -> separate);
    }

    public Splitter(boolean xSplit, Supplier<Double> separate, IDrawable first, IDrawable second) {
        super(Arrays.asList(first, second));
        this.xSplit = new ChangeableValue<>(this, xSplit);
        this.separate = new ChangeableValue<>(this, r -> separate.get());
    }

    public Splitter(double separate, IDrawable first, IDrawable second) {
        this(false, separate, first, second);
    }

    public Splitter(IDrawable first, IDrawable second) {
        this(false, 0.5, first, second);
    }

    public Splitter() {
        this(false, 0.5, null, null);
    }

    @Override
    protected boolean parentRedrawSatisfy() {
        return false;
    }

    public Splitter(boolean xSplit) {
        this(xSplit, 0.5, null, null);
    }

    public static Splitter threeWaySplit(boolean xSplit, double centerSeparate,
                                         IDrawable first, IDrawable center, IDrawable last) {
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

    public static Splitter threeWaySplit(boolean xSplit, Supplier<Double> centerSeparate,
                                         IDrawable first, IDrawable center, IDrawable last) {
        Supplier<Double> sep1 = new ValueOrSupplier.Transform<Double, Double>()
                .transform(new ValueOrSupplier<>(centerSeparate), cSep -> {
                    if (cSep <= -1) {
                        return (double) Math.round(-cSep / 2);
                    } else if (cSep < 0) {
                        return -cSep / 2;
                    } else if (cSep == 0) {
                        return Double.MIN_VALUE;
                    } else if (cSep < 1) {
                        return (1 + cSep) / -2;
                    } else {
                        return -cSep;
                    }
                });


        Supplier<Double> sep2 = new ValueOrSupplier.Transform<Double, Double>()
                .transform(new ValueOrSupplier<>(centerSeparate), cSep -> {
                    if (cSep <= -1) {
                        return (double) Math.round(cSep / 2);
                    } else if (cSep < 0) {
                        return cSep / (2 + cSep);
                    } else if (cSep < 1) {
                        return 2 * cSep / (1 + cSep);
                    } else {
                        return cSep;
                    }
                });

        return new Splitter(xSplit, sep1, first,
                new Splitter(xSplit, sep2, center, last));
    }

    public ChangeableValue<Boolean> getxSplit() {
        return xSplit;
    }

    public ChangeableValue<Function<Rectangle, Double>> getSeparate() {
        return separate;
    }

    public ChangeableValue<IDrawable> getFirst() {
        return get(0);
    }

    public ChangeableValue<IDrawable> getSecond() {
        return get(1);
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        Redraw redraw = needRedraw();
        boolean forcedRedraw = redraw.noNeedRedraw() || redraw.iNeedRedraw();

        Rectangle rectangle = canvas.getBoundingRectangle();

        boolean nowXSplit = xSplit.get();
        double nowSeparate = separate.get().apply(rectangle);

        Point dimension = nowXSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
        Point otherDimension = nowXSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector();

        double firstSize = rectangle.getSize().asPoint().multiple(dimension).asSize().getLongerDimension();
        if (nowSeparate <= -1) {
            firstSize = Math.max(0, firstSize + nowSeparate);
        } else if (nowSeparate <= 0) {
            firstSize *= 1 + nowSeparate;
        } else if (nowSeparate < 1) {
            firstSize *= nowSeparate;
        } else {
            firstSize = Math.min(firstSize, nowSeparate);
        }

        IDrawable child0 = getFirst().get();
        if (child0 != null) {
            canvas.clip(new Rectangle(
                    rectangle.getStart(),
                    rectangle.getStart().add(dimension.multiple(firstSize))
                            .add(otherDimension.multiple(rectangle.getSize().asPoint()))));
            if (forcedRedraw || getFirst().isChanged()) {
                child0.forceDraw(canvas);
            } else {
                child0.draw(canvas);
            }
            canvas.restore();
        }

        IDrawable child1 = getSecond().get();
        if (child1 != null) {
            canvas.clip(new Rectangle(
                    rectangle.getStart().add(dimension.multiple(firstSize)),
                    rectangle.getEnd()));

            if (forcedRedraw || getSecond().isChanged()) {
                child1.forceDraw(canvas);
            } else {
                child1.draw(canvas);
            }
            canvas.restore();
        }
    }
}
