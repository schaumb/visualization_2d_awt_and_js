package bxlx.system.input;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ValueOrSupplier;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.25..
 */
public abstract class OnOffClickable extends Clickable {
    protected ValueOrSupplier<Boolean> on = new ValueOrSupplier<>(false);

    @Override
    public void clicked() {
        super.clicked();
        on.setElem(!on.get());
    }

    public Supplier<Boolean> isOn() {
        return on.getAsSupplier();
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return new Redraw().setIf(on.isChanged(), Redraw.I_NEED_REDRAW);
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        on.commit();
    }

    public static class RectCheckBoxWith extends OnOffClickable {
        private final Rect rect;
        private final Splitter container;

        public RectCheckBoxWith(IDrawable other) {
            rect = new Rect();
            container = new Splitter(true, 0,
                    new Container(Arrays.asList(
                            new ColoredDrawable<>(rect, Color.BLACK),
                            new ColoredDrawable<>(
                                    new MarginDrawable<>(
                                            rect, 0.1, 0.1
                                    ), () -> disabled.get() ? Color.LIGHT_GRAY : inside.get() ? Color.DARK_GRAY : Color.GRAY
                            ),
                            new ColoredDrawable<>(
                                    new Text(() -> isOn().get() ? "✔" : "✘"),
                                    () -> disabled.get() ? Color.WHITE : Color.BLACK
                            )
                    ))
                    , other);
            container.getSeparate()
                    .setElem(r -> r.getSize().getHeight());
        }

        @Override
        public Redraw needRedraw() {
            return super.needRedraw().orIf(true, container.needRedraw());
        }

        @Override
        public void forceRedraw(ICanvas canvas) {
            container.setRedraw();
            container.forceDraw(canvas);
            super.forceDraw(canvas);
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return rect.isContains(
                    bound.getSize().getWidth() != bound.getSize().getLongerDimension() ? bound :
                            new AspectRatioDrawable<>(null, false, -1, -1, 1)
                                    .getClip().get().apply(bound)
                    , position);
        }
    }


    public static class ChangeImgClickable extends OnOffClickable {
        private final DrawImage disabledImg;
        private final DrawImage insideImg;
        private final DrawImage normalImg;
        private final DrawImage clickedImg;
        private final Predicate<Color> clickable;

        public ChangeImgClickable(String norm, String ins, String dis, Predicate<Color> clickable) {
            this(norm, ins, dis, norm, clickable);
        }

        public ChangeImgClickable(String norm, String ins, String dis, String clicked, Predicate<Color> clickable) {
            this.disabledImg = new DrawImage(dis);
            this.insideImg = new DrawImage(ins);
            this.normalImg = new DrawImage(norm);
            this.clickedImg = new DrawImage(clicked);
            this.clickable = clickable;
        }

        @Override
        public Redraw needRedraw() {
            return new Redraw();
        }

        private DrawImage getActualImage() {
            if (disabled.get()) {
                return disabledImg;
            } else if (inside.get()) {
                return insideImg;
            } else if (isOn().get()) {
                return clickedImg;
            } else {
                return normalImg;
            }
        }

        @Override
        public void forceRedraw(ICanvas canvas) {
            getActualImage().forceDraw(canvas);
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return clickable.test(normalImg.getColor(bound, position));
        }
    }

    public static class SameImgClickable extends OnOffClickable {
        private final DrawImage img;
        private final UnaryOperator<Rectangle> normalClip;
        private final UnaryOperator<Rectangle> insideClip;
        private final UnaryOperator<Rectangle> disableClip;
        private final UnaryOperator<Rectangle> clickedClip;
        private Rectangle lastRectangle;
        private final Predicate<Color> clickable;

        public SameImgClickable(String src, boolean xSplit, boolean hasClicked, Predicate<Color> clickable) {
            this(src,
                    xSplit ? Direction.RIGHT.getVector() : Direction.DOWN.getVector(),
                    xSplit ? Direction.DOWN.getVector() : Direction.RIGHT.getVector(), hasClicked ? 4.0 : 3.0, clickable);
        }

        private SameImgClickable(String src, Point split, Point notSplit, double part, Predicate<Color> clickable) {
            this(src,
                    r -> r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize()),
                    r -> r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize())
                            .withStart(r.getStart().add(r.getSize().asPoint().multiple(split.inverse()))),
                    r -> r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize())
                            .withStart(r.getStart().add(r.getSize().asPoint().multiple(split.inverse().multiple(2)))),
                    r -> {
                        Rectangle res = r.withSize(r.getSize().asPoint().multiple(split.multiple(part).add(notSplit)).asSize());
                        return part == 4.0 ? res.withStart(r.getStart().add(r.getSize().asPoint().multiple(split.inverse().multiple(3)))) : res;
                    }, clickable);
        }

        public SameImgClickable(String src, UnaryOperator<Rectangle> normalClip, UnaryOperator<Rectangle> insideClip, UnaryOperator<Rectangle> disableClip, Predicate<Color> clickable) {
            this(src, normalClip, insideClip, disableClip, normalClip, clickable);
        }

        public SameImgClickable(String src, UnaryOperator<Rectangle> normalClip, UnaryOperator<Rectangle> insideClip, UnaryOperator<Rectangle> disableClip, UnaryOperator<Rectangle> clickedClip, Predicate<Color> clickable) {
            this.img = new DrawImage(src);
            this.normalClip = normalClip;
            this.insideClip = insideClip;
            this.disableClip = disableClip;
            this.clickedClip = clickedClip;
            this.clickable = clickable;
        }

        private UnaryOperator<Rectangle> getActualClip() {
            if (disabled.get()) {
                return disableClip;
            } else if (inside.get()) {
                return insideClip;
            } else if (isOn().get()) {
                return clickedClip;
            } else {
                return normalClip;
            }
        }

        @Override
        public Redraw needRedraw() {
            return new Redraw();
        }

        @Override
        public void forceRedraw(ICanvas canvas) {
            lastRectangle = getActualClip().apply(canvas.getBoundingRectangle());

            canvas.fakeClip(lastRectangle);
            img.forceDraw(canvas);
            canvas.fakeRestore();
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return lastRectangle != null && clickable.test(img.getColor(lastRectangle, position));
        }
    }
}
