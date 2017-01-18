package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawArc;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.DrawNumber;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.SplitContainer;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Stick;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.input.Button;
import bxlx.system.input.DiscreteSlider;
import bxlx.system.input.Slider;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Builder<T extends IDrawable> {
    private T wrapped;

    public Builder(T wrapped) {
        this.wrapped = wrapped;
    }

    public T get() {
        return wrapped;
    }

    public static <T extends IDrawable> Builder<T> make(T start) {
        if (start == null) {
            return null;
        }
        return new Builder<>(start);
    }

    public Builder<ColoredDrawable> makeColored(Color color) {
        return new Builder<>(new ColoredDrawable<>(get(), color));
    }

    public Builder<ColoredDrawable> makeColored(Supplier<Color> color) {
        return new Builder<>(new ColoredDrawable<>(get(), color));
    }

    public Builder<MarginDrawable> makeMargin(double margin) {
        return makeMargin(margin, margin);
    }

    public Builder<MarginDrawable> makeMargin(double marginX, double marginY) {
        return new Builder<>(new MarginDrawable<>(get(), marginX, marginY));
    }

    public Builder<MarginDrawable> makeMargin(Supplier<Double> marginX, Supplier<Double> marginY) {
        return new Builder<>(new MarginDrawable<>(get(), marginX, marginY));
    }

    public Builder<AspectRatioDrawable> makeSquare(int alignX, int alignY) {
        return makeAspect(alignX, alignY, 1);
    }

    public Builder<AspectRatioDrawable> makeAspect(int alignX, int alignY, double ratio) {
        return makeAspect(alignX, alignY, () -> ratio);
    }

    public Builder<AspectRatioDrawable> makeAspect(int alignX, int alignY, Supplier<Double> ratio) {
        return new Builder<>(new AspectRatioDrawable<>(get(), false, alignX, alignY, ratio));
    }

    public Builder<ClippedDrawable> makeClipped(UnaryOperator<Rectangle> clip) {
        return new Builder<>(new ClippedDrawable<>(get(), false, clip));
    }

    public static <T extends VisibleDrawable.VisibleDraw> Builder<VisibleDrawable> makeVisible(
            Builder<T> builder,
            boolean visibility) {
        return new Builder<>(new VisibleDrawable<>(builder.get(), visibility));
    }

    public static <T extends VisibleDrawable.VisibleDraw> Builder<VisibleDrawable> makeVisible(
            Builder<T> builder,
            Supplier<Boolean> visibility) {
        return new Builder<>(new VisibleDrawable<>(builder.get(), visibility));
    }

    public Builder<Container> makeBackgrounded(Color color) {
        Builder<Container> result = new Builder<>(new Container(new ArrayList<>(), 1));
        result.get().add(background().makeColored(color).get());
        result.get().add(get());
        return result;
    }

    public static Builder<DrawRectangle> background() {
        return new Builder<>(new DrawRectangle());
    }

    public static Builder<Text> text(String text) {
        return new Builder<>(new Text(text));
    }

    public static Builder<Text> text(String text, String referenceText) {
        return new Builder<>(new Text(text, referenceText));
    }

    public static Builder<DrawArc> circle(boolean inside) {
        return new Builder<>(DrawArc.circle(inside));
    }

    public static Builder<DrawArc> arc(boolean inside, double fromRadius, double toRadius) {
        return new Builder<>(new DrawArc(inside, fromRadius, toRadius));
    }

    public static Builder<DrawNumber> number(int number) {
        return number(number, "", null);
    }

    public static Builder<DrawNumber> number(int number, String suffix, String preferenceText) {
        return new Builder<>(new DrawNumber(number, suffix, preferenceText));
    }

    public static Builder<DrawNGon> nGon(boolean inside, boolean ellipse, int n, double startAngle, boolean makeCentered) {
        return new Builder<>(new DrawNGon(inside, ellipse, n, startAngle, makeCentered));
    }

    public static Builder<Stick> stick(double angle, double length, double thickness, IDrawable start, IDrawable end) {
        return new Builder<>(new Stick(angle, length, thickness, start, end));
    }

    public static Builder<SplitContainer> container(boolean xSplit) {
        return new Builder<>(new SplitContainer(xSplit));
    }

    public static Builder<Container> container() {
        return new Builder<>(new Container());
    }

    public static Builder<Splitter> splitter(boolean xSplit, double separate, IDrawable first, IDrawable second) {
        return new Builder<>(new Splitter(xSplit, separate, first, second));
    }

    public static Builder<Splitter> threeWaySplit(boolean xSplit, double centerSeparate,
                                                  IDrawable first, IDrawable center, IDrawable last) {
        return new Builder<>(Splitter.threeWaySplit(xSplit, centerSeparate, first, center, last));
    }

    public static Builder<Button> button(IDrawable drawable, Consumer<Button> atClick, Consumer<Button> atHold, Supplier<Boolean> disabled) {
        return new Builder<>(new Button(drawable, atClick, atHold, disabled));
    }

    public static Builder<Slider> slider(boolean xDraw, double start, Color bg, Supplier<Boolean> disabled) {
        return new Builder<>(new Slider(xDraw, start, bg, disabled));
    }

    public static Builder<DiscreteSlider> discreteSlider(boolean xDraw, Color bg, int from, int to, int now, Supplier<Boolean> disabled) {
        return new Builder<>(new DiscreteSlider(xDraw, bg, from, to, now, disabled));
    }

    public static Builder<Navigator> navigator(Button upLeft, Button upRight, IDrawable main, Supplier<Boolean> visibility, double buttonsThick, Color background) {
        return new Builder<>(new Navigator(upLeft, upRight, main, visibility, buttonsThick, background));
    }

    public static Builder<DrawImage> image(String src) {
        return new Builder<>(new DrawImage(src));
    }

    public static Builder<AspectRatioDrawable> imageKeepAspectRatio(String src, int alignX, int alignY) {
        DrawImage img = new DrawImage(src);
        return new Builder<>(new AspectRatioDrawable<>(img, false, alignX, alignY, () -> img.getOriginalAspectRatio()));
    }
}
