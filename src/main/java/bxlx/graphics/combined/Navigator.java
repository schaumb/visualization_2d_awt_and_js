package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.DrawRectangle;
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
        return new MarginDrawable(button, 3);
    }

    private final WrapperClass mainWrapper;
    private double zoom = 1;
    private double shiftX = 0;
    private double shiftY = 0;

    public Navigator(Button upLeft, Button upRight, IDrawable main, double buttonsThick, Color background) {
        super(new Container());

        getChild().add(new ColoredDrawable(new DrawRectangle(), background));
        getChild().add(
                Splitter.threeWaySplit(false, -buttonsThick * 2,
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(upLeft),
                                makeButton(new Button(new DrawNGon(3, Math.PI / 2, true),
                                        null, b -> up(), () -> shiftY <= 0)),
                                makeButton(upRight)),
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(new Button(new DrawNGon(3, Math.PI, true),
                                        null, b -> left(), () -> shiftX <= 0)),
                                mainWrapper = new WrapperClass(main),
                                makeButton(new Button(new DrawNGon(3, 0, true),
                                        null, b -> right(), () -> zoom - 1 <= shiftX))),
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(false)),
                                        null, b -> zoomOut(), () -> zoom <= 1)),
                                makeButton(new Button(new DrawNGon(3, -Math.PI / 2, true),
                                        null, b -> down(), () -> zoom - 1 <= shiftY)),
                                makeButton(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(true)),
                                        null, b -> zoomIn(), null)))
                )
        );
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

    private class WrapperClass extends DrawableWrapper {
        public WrapperClass(IDrawable wrapped) {
            super(wrapped);
        }

        @Override
        public void forceRedraw(ICanvas canvas) {
            Rectangle bound = canvas.getBoundingRectangle();

            Size size = bound.getSize().asPoint().multiple(zoom).asSize();

            Point start = bound.getStart().add(bound.getSize().asPoint().negate().multiple(new Point(shiftX, shiftY)));
            Rectangle fakeBound = new Rectangle(start, size);

            canvas.fakeClip(fakeBound);
            super.forceRedraw(canvas);
            canvas.fakeRestore();
        }
    }
}
