package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.ZoomDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.Stick;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.IMouseEventListener;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.18..
 */
public class Slider extends Container implements IMouseEventListener {
    private Point draggedPoint;
    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final Button button;
    private final ChangeableDrawable.ChangeableValue<Boolean> xDraw;
    private final ChangeableValue<Double> now;

    public Slider(boolean xDraw, double start, Color bg, Supplier<Boolean> disabled) {
        super(new ArrayList<>(), 2);

        this.xDraw = new ChangeableValue<>(this, xDraw);
        this.now = new ChangeableValue<>(this, Math.max(0, Math.min(1, start)));

        Stick mainStick = new Stick(0, 0.1, 0.5, null, null);
        mainStick.getAngle().setSupplier(() -> this.xDraw.get() ? 0 : Math.PI / 2);

        add(new ColoredDrawable<>(new DrawRectangle(), bg));
        add(new ColoredDrawable<>(mainStick, Color.LIGHT_GRAY));
        add(new ClippedDrawable<>(new ZoomDrawable<>(new AspectRatioDrawable<>(
                new ColoredDrawable<>(button = new Button(null, null, null, disabled), Color.GRAY)
                , true, -1, -1, () -> this.xDraw.get() ? 2 : 0.5)
                , true, () -> 1.0, () -> this.xDraw.get() ? 0.05 + now.get() * 0.85 : 0, () -> this.xDraw.get() ? 0 : 0.05 + now.get() * 0.85)
                , true, r -> lastRectangle = mainStick.getBoundingRectangle(r)));

        SystemSpecific.get().setMouseEventListenerQueue(this);
    }

    public ChangeableValue<Boolean> getxDraw() {
        return xDraw;
    }

    public ChangeableValue<Double> getNow() {
        return now;
    }

    @Override
    public void move(Point position) {
        if (draggedPoint != null && lastRectangle.isContains(position) && MouseInfo.get().isLeftClicked() &&
                !button.getDisabled().get()) {
            Point p = this.xDraw.get() ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
            Point percent = position.add(draggedPoint.negate()).multiple(p).multiple(lastRectangle.getSize().asPoint().multiple(0.85).inverse());

            double d = percent.getX();
            if (d == 0.0) {
                d = percent.getY();
            }
            double nowP = now.get() + d;
            double real;
            now.setElem(real = Math.max(0, Math.min(1, nowP)));

            if (real == nowP) {
                draggedPoint = position;
            } else {
                draggedPoint = button.getLastRectangle().get().getCenter();
            }
        }
    }

    @Override
    public void down(Point where, boolean leftButton) {
        if (lastRectangle != null && leftButton && button.isInsideNow() && !button.getDisabled().get()) {
            draggedPoint = where;
        }
    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (leftButton) {
            draggedPoint = null;
        }
    }

}