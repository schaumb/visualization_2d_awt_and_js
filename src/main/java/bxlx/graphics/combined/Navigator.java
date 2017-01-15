package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.Magnifying;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Stick;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.Button;
import bxlx.system.MouseInfo;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Navigator extends DrawableWrapper<Container> {

    private IDrawable makeMargin(IDrawable drawable) {
        if (drawable == null) {
            return null;
        }
        return new MarginDrawable(drawable, 3);
    }

    private final ChangeableValue<Boolean> vis;
    private final WrapperClass mainWrapper;
    private double zoom = 1;
    private double shiftX = 0;
    private double shiftY = 0;

    public Navigator(Button upLeft, Button upRight, IDrawable main, Supplier<Boolean> visibility, double buttonsThick, Color background) {
        super(null);

        mainWrapper = new WrapperClass(main, background);
        vis = new ChangeableValue<>(this, visibility);
        Supplier<Double> buttThick = () -> visibility.get() ? -buttonsThick * 2 : 0;
        Supplier<Boolean> novisibility = () -> !visibility.get();
        Container container = new Container();
        container.add(new ColoredDrawable(new DrawRectangle(), background));
        container.add(new Container(Arrays.asList(
                new VisibleDrawable(mainWrapper, novisibility),
                Splitter.threeWaySplit(false, -buttonsThick * 2,
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeMargin(new VisibleDrawable(upLeft, () -> {
                                    if (upLeft == null) return false;
                                    Rectangle r = upLeft.getLastRectangle().get();
                                    if (r == null || visibility.get()) return true;

                                    r = r.withSize(r.getSize().asPoint().multiple(2).asSize());

                                    return r.isContains(MouseInfo.get().getPosition());
                                })),
                                null,
                                makeMargin(new VisibleDrawable(upRight, () -> {
                                    if (upRight == null) return false;
                                    Rectangle r = upRight.getLastRectangle().get();
                                    if (r == null || visibility.get()) return true;

                                    Size size = r.getSize().asPoint().multiple(2).asSize();
                                    Point start = r.getStart().add(Direction.LEFT.getVector().multiple(r.getSize().asPoint()));

                                    return new Rectangle(start, size).isContains(MouseInfo.get().getPosition());
                                }))),
                        null, null)), 1
        ));
        container.add(
                Splitter.threeWaySplit(false, buttThick,
                        Splitter.threeWaySplit(true, buttThick,
                                null,
                                makeMargin(new VisibleDrawable(new Button(new DrawNGon(3, Math.PI / 2, true),
                                        null, b -> up(), () -> shiftY <= 0), visibility)),
                                null),
                        Splitter.threeWaySplit(true, buttThick,
                                makeMargin(new VisibleDrawable(new Button(new DrawNGon(3, Math.PI, true),
                                        null, b -> left(), () -> shiftX <= 0), visibility)),
                                new VisibleDrawable(mainWrapper, visibility),
                                makeMargin(new VisibleDrawable(new Button(new DrawNGon(3, 0, true),
                                        null, b -> right(), () -> zoom - 1 <= shiftX), visibility))),
                        Splitter.threeWaySplit(true, buttThick,
                                makeMargin(new VisibleDrawable(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(false)),
                                        null, b -> zoomOut(), () -> zoom <= 1), visibility)),
                                makeMargin(new VisibleDrawable(new Button(new DrawNGon(3, -Math.PI / 2, true),
                                        null, b -> down(), () -> zoom - 1 <= shiftY), visibility)),
                                makeMargin(new VisibleDrawable(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(true)),
                                        null, b -> zoomIn(), null), visibility)))
                )
        );

        getChild().setElem(container);
    }

    private void setChanged() {
        mainWrapper.setRedraw();
    }

    private void zoomIn() {
        zoom *= 1.1;

        if (zoom == 1.1) {
            shiftY = shiftX = 0.05;
        } else {
            shiftX = shiftX / (zoom / 1.1 - 1) * (zoom - 1);
            shiftY = shiftY / (zoom / 1.1 - 1) * (zoom - 1);
        }

        setChanged();
    }

    private void zoomOut() {
        shiftX = Math.max(0, shiftX / (zoom - 1) * (zoom / 1.1 - 1));
        shiftY = Math.max(0, shiftY / (zoom - 1) * (zoom / 1.1 - 1));
        zoom = Math.max(1, zoom / 1.1);
        setChanged();
    }

    private void up() {
        shiftY = Math.max(0, shiftY - (zoom - 1) / zoom);
        setChanged();
    }

    private void down() {
        shiftY = Math.min(zoom - 1, shiftY + (zoom - 1) / zoom);
        setChanged();
    }

    private void left() {
        shiftX = Math.max(0, shiftX - (zoom - 1) / zoom);
        setChanged();
    }

    private void right() {
        shiftX = Math.min(zoom - 1, shiftX + (zoom - 1) / zoom);
        setChanged();
    }

    private class WrapperClass extends DrawableWrapper implements VisibleDrawable.VisibleDraw {
        private Color bg;

        public WrapperClass(IDrawable wrapped, Color bg) {
            super(wrapped);
            this.bg = bg;
        }

        @Override
        public void forceRedraw(ICanvas canvas) {
            canvas.clearCanvas(bg);
            Rectangle bound = canvas.getBoundingRectangle();

            Size size = bound.getSize().asPoint().multiple(zoom).asSize();

            Point start = bound.getStart().add(bound.getSize().asPoint().negate().multiple(new Point(shiftX, shiftY)));
            Rectangle fakeBound = new Rectangle(start, size);

            canvas.fakeClip(fakeBound);
            super.forceRedraw(canvas);
            canvas.fakeRestore();
        }

        @Override
        public void noVisibleDraw(ICanvas canvas) {

        }
    }
}
