package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.Magnifying;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Stick;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.Button;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Navigator extends DrawableWrapper<Container> {

    private IDrawable makeButton(Button button) {
        if (button == null) {
            return null;
        }
        return Builder.make(button).makeMargin(3);
    }

    private boolean changed = false;
    private double zoom = 1;
    private double shiftX = 0;
    private double shiftY = 0;

    public Navigator(Button upLeft, Button upRight, IDrawable main, double buttonsThick, Color background) {
        super(Builder.container().getWrapped());

        getWrapped().add(Builder.background().makeColored(background));
        getWrapped().add(
                Splitter.threeWaySplit(false, -buttonsThick * 2,
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(upLeft),
                                makeButton(new Button(new DrawNGon(3, Math.PI / 2, true),
                                        null, () -> up(), () -> shiftY <= 0)),
                                makeButton(upRight)),
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(new Button(new DrawNGon(3, Math.PI, true),
                                        null, () -> left(), () -> shiftX <= 0)),
                                new WrapperClass(main),
                                makeButton(new Button(new DrawNGon(3, 0, true),
                                        null, () -> right(), () -> zoom - 1 <= shiftX))),
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(false)),
                                        null, () -> zoomOut(), () -> zoom <= 1)),
                                makeButton(new Button(new DrawNGon(3, -Math.PI / 2, true),
                                        null, () -> down(), () -> zoom - 1 <= shiftY)),
                                makeButton(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(true)),
                                        null, () -> zoomIn(), null)))
                )
        );
    }

    private void setChanged() {
        changed = true;
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

    //TODO precízebb lépésközök
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

    private class WrapperClass extends DrawableWrapper {
        public WrapperClass(IDrawable wrapped) {
            super(wrapped);
        }

        @Override
        public boolean needRedraw() {
            return changed || super.needRedraw();
        }

        @Override
        public void forceDraw(ICanvas canvas) {
            boolean forcedRedraw = !needRedraw() || changed;

            Rectangle bound = canvas.getBoundingRectangle();

            Size size = bound.getSize().asPoint().multiple(zoom).asSize();

            Point start = bound.getStart().add(bound.getSize().asPoint().negate().multiple(new Point(shiftX, shiftY)));
            Rectangle fakeBound = new Rectangle(start, size);

            canvas.fakeClip(fakeBound);
            if (forcedRedraw) {
                super.forceDraw(canvas);
            } else {
                getWrapped().draw(canvas);
            }
            canvas.fakeRestore();

            changed = false;
        }
    }
}
