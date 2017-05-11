package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.SizeChangeableContainer;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.container.TransformerContainer;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.drawable.ShapeClippedDrawable;
import bxlx.graphics.drawable.ShapeDrawable;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.fill.DrawArc;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.DrawNumber;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;
import bxlx.graphics.shapes.Shape;
import bxlx.system.ColorScheme;
import bxlx.system.FPS;
import bxlx.system.functional.ValueOrSupplier;
import bxlx.system.input.Button;
import bxlx.system.input.DiscreteSlider;
import bxlx.system.input.Slider;
import bxlx.system.input.clickable.ColorSchemeClickable;
import bxlx.system.input.clickable.CursorChangeClickable;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
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

    public ContainerBuilder<IDrawable, Container<IDrawable>> withFPS() {
        return container().add(get()).add(new FPS());
    }

    public static Builder<ShapeDrawable> shape(Shape shape) {
        return new Builder<>(new ShapeDrawable(shape));
    }

    public static class ContainerBuilder<T extends IDrawable, C extends SizeChangeableContainer<T, ?>> extends Builder<C> {
        public ContainerBuilder(C wrapped) {
            super(wrapped);
        }

        public <U extends IDrawable> TransformContainerBuilder<U, T, C> transform(Function<U, T> transform) {
            return new TransformContainerBuilder<>(new TransformerContainer<>(get(), transform));
        }

        public ContainerBuilder<T, C> add(T elem) {
            get().add(elem);
            return this;
        }

        public ContainerBuilder<T, C> add(Supplier<T> elem) {
            get().add(elem);
            return this;
        }

        public ContainerBuilder<T, C> add(Builder<T> elem) {
            get().add(elem.get());
            return this;
        }
    }

    public static class TransformContainerBuilder<U extends IDrawable, T extends IDrawable, C extends SizeChangeableContainer<T, ?>> extends
            Builder<TransformerContainer<U, T, C>> {
        public TransformContainerBuilder(TransformerContainer<U, T, C> wrapped) {
            super(wrapped);
        }

        public <V extends IDrawable> TransformContainerBuilder<V, T, C> transform(Function<V, U> transform) {
            Function<U, T> fun = get().getTransformFunction();
            return new TransformContainerBuilder<>(new TransformerContainer<>(get().getChild().get(),
                    e -> fun.apply(transform.apply(e))));
        }

        public TransformContainerBuilder<U, T, C> add(U elem) {
            get().add(elem);
            return this;
        }

        public TransformContainerBuilder<U, T, C> add(Builder<U> elem) {
            get().add(elem.get());
            return this;
        }
    }

    public Builder<ColoredDrawable<T>> makeColored(Color color) {
        return new Builder<>(new ColoredDrawable<>(get(), color));
    }

    public Builder<ColoredDrawable<T>> makeColored(Supplier<Color> color) {
        return new Builder<>(new ColoredDrawable<>(get(), color));
    }

    public Builder<MarginDrawable> makeMargin(double margin) {
        return makeMargin(margin, margin);
    }

    public Builder<MarginDrawable> makeMargin(double marginX, double marginY) {
        return new Builder<>(new MarginDrawable<>(get(), marginX, marginY));
    }

    public Builder<AspectRatioDrawable> makeSquare(int alignX, int alignY) {
        return makeAspect(alignX, alignY, 1);
    }

    public Builder<AspectRatioDrawable<T>> makeAspect(boolean fake, int alignX, int alignY, Function<T, Double> ratio) {
        return new Builder<>(new AspectRatioDrawable<>(get(), fake, alignX, alignY, ratio));
    }

    public Builder<AspectRatioDrawable> makeAspect(int alignX, int alignY, double ratio) {
        return new Builder<>(new AspectRatioDrawable<>(get(), false, alignX, alignY, ratio));
    }

    public Builder<AspectRatioDrawable> makeAspect(int alignX, int alignY, Supplier<Double> ratio) {
        return new Builder<>(new AspectRatioDrawable<>(get(), false, alignX, alignY, ratio));
    }

    public Builder<ClippedDrawable<T>> makeClipped(UnaryOperator<Rectangle> clip) {
        return new Builder<>(new ClippedDrawable<>(get(), false, clip));
    }

    public Builder<ClippedDrawable<T>> makeClipped(boolean fake, UnaryOperator<Rectangle> clip) {
        return new Builder<>(new ClippedDrawable<>(get(), fake, clip));
    }
    public Builder<ShapeClippedDrawable<T>> makeShapeClipped(Shape shape) {
        return new Builder<>(new ShapeClippedDrawable<>(get(), shape));
    }

    public Builder<T> applyer(Consumer<T> consumer) {
        consumer.accept(get());
        return this;
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

    public ContainerBuilder<IDrawable, Container<IDrawable>> makeBackgrounded(Color color) {
        ContainerBuilder<IDrawable, Container<IDrawable>> result = new ContainerBuilder<>(new Container<>(new ArrayList<>(), 1));
        result.get().add(background().makeColored(color).get());
        result.get().add(get());
        return result;
    }

    public ContainerBuilder<IDrawable, Container<IDrawable>> toContainer() {
        ContainerBuilder<IDrawable, Container<IDrawable>> result = new ContainerBuilder<>(new Container<>(new ArrayList<>(), 1));
        result.get().add(get());
        return result;
    }

    public static Builder<DrawRectangle> background() {
        return new Builder<>(new DrawRectangle());
    }

    public static Builder<ColoredDrawable<Text>> text(String text) {
        return new Builder<>(new Text(text)).makeColored(ColorScheme.getCurrentColorScheme().textColor);
    }

    public static Builder<ColoredDrawable<Text>> text(String text, int align) {
        return new Builder<>(new Text(text, (String) null, align)).makeColored(ColorScheme.getCurrentColorScheme().textColor);
    }

    public static Builder<ColoredDrawable<Text>> text(String text, String referenceText) {
        return new Builder<>(new Text(text, referenceText, 0)).makeColored(ColorScheme.getCurrentColorScheme().textColor);
    }

    public static Builder<Text> text(String text, String referenceText, int align) {
        return new Builder<>(new Text(text, referenceText, align));
    }

    public static Builder<Text> text(String text, Supplier<String> referenceText, int align) {
        return new Builder<>(new Text(text, referenceText, align));
    }

    public static Builder<Text> text(Supplier<String> text, Supplier<String> referenceText, int align) {
        return new Builder<>(new Text(text, referenceText, align));
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
        return new Builder<>(new DrawNumber(number, suffix, preferenceText, 0));
    }

    public static Builder<DrawNGon> nGon(boolean inside, boolean ellipse, int n, double startAngle, boolean makeCentered) {
        return new Builder<>(new DrawNGon(inside, ellipse, n, startAngle, makeCentered));
    }

    public static Builder<Stick> stick(double angle, double length, double thickness, IDrawable start, IDrawable end) {
        return new Builder<>(new Stick(angle, length, thickness, start, end));
    }

    public static <T extends IDrawable> ContainerBuilder<T, SplitContainer<T>> container(boolean xSplit) {
        return new ContainerBuilder<>(new SplitContainer<>(xSplit));
    }

    public static <T extends IDrawable> ContainerBuilder<T, Container<T>> container() {
        return new ContainerBuilder<>(new Container<>());
    }

    public static Builder<Splitter> splitter(boolean xSplit, double separate, IDrawable first, IDrawable second) {
        return new Builder<>(new Splitter(xSplit, separate, first, second));
    }

    public static Builder<Splitter> splitter(boolean xSplit, Supplier<Double> separate, IDrawable first, IDrawable second) {
        return new Builder<>(new Splitter(xSplit, separate, first, second));
    }

    public static Builder<Splitter> threeWaySplit(boolean xSplit, double centerSeparate,
                                                  IDrawable first, IDrawable center, IDrawable last) {
        return new Builder<>(Splitter.threeWaySplit(xSplit, centerSeparate, first, center, last));
    }

    public static Builder<Slider> slider(boolean xDraw, double start, Supplier<Boolean> disabled) {
        return new Builder<>(new Slider(xDraw, start, disabled));
    }

    public static Builder<DiscreteSlider> discreteSlider(boolean xDraw, int from, int to, int now, Supplier<Boolean> disabled) {
        return new Builder<>(new DiscreteSlider(xDraw, from, to, now, disabled));
    }

    public static Builder<Navigator> navigator(Button upLeft, Button upRight, IDrawable main, Supplier<Boolean> visibility, double buttonsThick, Color background) {
        return new Builder<>(new Navigator(upLeft, upRight, main, visibility, buttonsThick, background));
    }

    public static Builder<DrawImage> image(String src) {
        return new Builder<>(new DrawImage(src));
    }

    public static Builder<AspectRatioDrawable<DrawImage>> imageKeepAspectRatio(String src, int alignX, int alignY) {
        DrawImage img = new DrawImage(src);
        return new Builder<>(new AspectRatioDrawable<>(img, false, alignX, alignY, () -> img.getOriginalAspectRatio()));
    }

    public static class ButtonBuilder<T extends Button<?>> extends Builder<T> {
        public ButtonBuilder(T wrapped) {
            super(wrapped);
        }

        public Builder<Container<IDrawable>> with(IDrawable drawable) {
            return Builder.container().add(get()).add(drawable);
        }

        public Builder<Container<IDrawable>> with(double margin, IDrawable drawable) {
            return Builder.container().add(get()).add(new Builder<>(drawable).makeMargin(margin).get());
        }

        public Builder<Container<IDrawable>> with(double marginX, double marginY, IDrawable drawable) {
            return Builder.container().add(get()).add(new Builder<>(drawable).makeMargin(marginX, marginY).get());
        }

    }

    public static class ClickableBuilder<T extends Button.Clickable> extends Builder<T> {
        public ClickableBuilder(T wrapped) {
            super(wrapped);
        }

        public ButtonBuilder<Button<T>> makeButton() {
            return new ButtonBuilder<>(new Button<>(get(), null, null, () -> false));
        }

        public ButtonBuilder<Button<T>> makeButton(Consumer<Button<T>> atClick) {
            return new ButtonBuilder<>(new Button<>(get(), atClick, null, () -> false));
        }

        public ButtonBuilder<Button<T>> makeButton(Consumer<Button<T>> atClick, Consumer<Button<T>> atHold, Supplier<Boolean> disabled) {
            return new ButtonBuilder<>(new Button<>(get(), atClick, atHold, disabled));
        }
    }

    public static ClickableBuilder<CursorChangeClickable> getCursorChangeClickable() {
        return new ClickableBuilder<>(new CursorChangeClickable());
    }

    public static ClickableBuilder<ColorSchemeClickable> getColorSchemeClickable(boolean clickedNeed) {
        return new ClickableBuilder<>(new ColorSchemeClickable(false, clickedNeed));
    }

    public ValueOrSupplier<T> makeValueOrSupplier() {
        return new ValueOrSupplier<>(get());
    }
}
