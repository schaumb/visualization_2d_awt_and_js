package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.drawable.ZoomDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.Magnifying;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Stick;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.MouseInfo;
import bxlx.system.input.Button;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Navigator extends DrawableWrapper<Container> {

    private IDrawable makeMargin(IDrawable drawable) {
        if (drawable == null) {
            return null;
        }
        return new MarginDrawable<>(drawable, 3);
    }

    private double zoom = 1;
    private double shiftX = 0;
    private double shiftY = 0;

    private final DrawableWrapper<IDrawable> mainWrapper;
    private final DrawableWrapper<Button> upLeftWrapper;
    private final DrawableWrapper<Button> upRightWrapper;

    public Navigator(Button upLeft, Button upRight, IDrawable main, Supplier<Boolean> visibility, double buttonsThick, Color background) {
        super(new Container(new ArrayList<>(), 2));

        Supplier<Double> buttThick = () -> visibility.get() ? -buttonsThick * 2 : 0;

        getChild().get().add(new ColoredDrawable<>(new DrawRectangle(), background));
        getChild().get().add(
                Splitter.threeWaySplit(false, buttThick,
                        Splitter.threeWaySplit(true, buttThick,
                                null,
                                makeMargin(new Button(new DrawNGon(3, Math.PI / 2, true),
                                        null, b -> up(), () -> shiftY <= 0)),
                                null),
                        Splitter.threeWaySplit(true, buttThick,
                                makeMargin(new Button(new DrawNGon(3, Math.PI, true),
                                        null, b -> left(), () -> shiftX <= 0)),
                                mainWrapper = new ZoomDrawable<>(main, true, () -> zoom, () -> -shiftX, () -> -shiftY),
                                makeMargin(new Button(new DrawNGon(3, 0, true),
                                        null, b -> right(), () -> zoom - 1 <= shiftX))),
                        Splitter.threeWaySplit(true, buttThick,
                                makeMargin(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(false)),
                                        null, b -> zoomOut(), () -> zoom <= 1)),
                                makeMargin(new Button(new DrawNGon(3, -Math.PI / 2, true),
                                        null, b -> down(), () -> zoom - 1 <= shiftY)),
                                makeMargin(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(true)),
                                        null, b -> zoomIn(), null)))
                )
        );
        getChild().get().add(Splitter.threeWaySplit(false, -buttonsThick * 2,
                Splitter.threeWaySplit(true, -buttonsThick * 2,
                        makeMargin(upLeftWrapper = new VisibleDrawable<>(upLeft, () -> {
                            if (upLeft == null) return false;
                            Rectangle r = upLeft.getLastRectangle().get();
                            if (r == null || visibility.get()) return true;

                            r = r.withSize(r.getSize().asPoint().multiple(2).asSize());

                            return r.isContains(MouseInfo.get().getPosition());
                        })),
                        null,
                        makeMargin(upRightWrapper = new VisibleDrawable<>(upRight, () -> {
                            if (upRight == null) return false;
                            Rectangle r = upRight.getLastRectangle().get();
                            if (r == null || visibility.get()) return true;

                            Size size = r.getSize().asPoint().multiple(2).asSize();
                            Point start = r.getStart().add(Direction.LEFT.getVector().multiple(r.getSize().asPoint()));

                            return new Rectangle(start, size).isContains(MouseInfo.get().getPosition());
                        }))),
                null, null));
    }

    public ChangeableValue<IDrawable> getMain() {
        return mainWrapper.getChild();
    }

    public ChangeableValue<Button> getUpLeft() {
        return upLeftWrapper.getChild();
    }

    public ChangeableValue<Button> getUpRight() {
        return upRightWrapper.getChild();
    }

    private void zoomIn() {
        zoom *= 1.1;

        if (zoom == 1.1) {
            shiftY = shiftX = 0.05;
        } else {
            shiftX = shiftX / (zoom / 1.1 - 1) * (zoom - 1);
            shiftY = shiftY / (zoom / 1.1 - 1) * (zoom - 1);
        }
    }

    private void zoomOut() {
        shiftX = Math.max(0, shiftX / (zoom - 1) * (zoom / 1.1 - 1));
        shiftY = Math.max(0, shiftY / (zoom - 1) * (zoom / 1.1 - 1));
        zoom = Math.max(1, zoom / 1.1);
    }

    private void up() {
        shiftY = Math.max(0, shiftY - (zoom - 1) / zoom);
    }

    private void down() {
        shiftY = Math.min(zoom - 1, shiftY + (zoom - 1) / zoom);
    }

    private void left() {
        shiftX = Math.max(0, shiftX - (zoom - 1) / zoom);
    }

    private void right() {
        shiftX = Math.min(zoom - 1, shiftX + (zoom - 1) / zoom);
    }
}
