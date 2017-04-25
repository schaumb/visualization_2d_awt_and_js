package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Stick;
import bxlx.graphics.container.Container;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.ZoomDrawable;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.IMouseEventListener;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.input.clickable.RectClickable;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.18..
 */
public class Slider extends DrawableWrapper<Container<IDrawable>> implements IMouseEventListener {
    private Point draggedPoint;
    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final Button<?> button;
    private final ChangeableDrawable.ChangeableValue<Boolean> xDraw;
    private final ChangeableValue<Double> now;

    public Slider(boolean xDraw, double start, Supplier<Boolean> disabled) {
        this(new Button<>(new RectClickable(null), null, null, disabled), xDraw, start);
    }

    public Slider(Button<?> button, boolean xDraw, double start) {
        super(new Container<>());

        this.button = button;
        this.xDraw = new ChangeableValue<>(this, xDraw);
        this.now = new ChangeableValue<>(this, Math.max(0, Math.min(1, start)));

        Stick mainStick = new Stick(0, 0.1, 0.5, null, null);
        mainStick.getAngle().setSupplier(() -> this.xDraw.get() ? 0 : Math.PI / 2);
        mainStick.getLength().setDepFun(r -> r.getSize().getWidth() / r.getSize().getHeight());

        getChild().get().add(new ColoredDrawable<>(mainStick, Color.LIGHT_GRAY));
        getChild().get().add(new ClippedDrawable<>(new ZoomDrawable<>(new AspectRatioDrawable<>(
                new ColoredDrawable<>(this.button, Color.GRAY)
                , true, -1, -1, () -> this.xDraw.get() ? 2 : 0.5)
                , true, r -> 1.0, r -> this.xDraw.get() ? now.get() * (1 - 3 * r.getSize().getHeight() / r.getSize().getWidth()) : 0.0,
                r -> this.xDraw.get() ? 0.0 : 0.5 * r.getSize().getWidth() / r.getSize().getHeight() + now.get() * (1 - 1.5 * r.getSize().getWidth() / r.getSize().getHeight()))
                , true, r -> lastRectangle = mainStick.getBoundingRectangle(r)));

        SystemSpecific.get().setMouseEventListenerQueue(this);
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().setIf(now.isChanged(), Redraw.PARENT_NEED_REDRAW);
    }

    public ChangeableValue<Boolean> getxDraw() {
        return xDraw;
    }

    public ChangeableValue<Double> getNow() {
        return now;
    }

    public ChangeableValue<Boolean> getDisable() {
        return button.getDisabled();
    }

    @Override
    public void move(Point position) {
        if (draggedPoint != null && lastRectangle.isContains(position) && MouseInfo.get().isLeftClicked() &&
                !button.getDisabled().get()) {
            Point p = this.xDraw.get() ? Direction.RIGHT.getVector() : Direction.DOWN.getVector();
            Point percent = position.add(draggedPoint.negate()).multiple(p).multiple(lastRectangle.getSize().asPoint().multiple(1 - 1.5 * lastRectangle.getSize().getWidth() / lastRectangle.getSize().getHeight()).inverse());

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
