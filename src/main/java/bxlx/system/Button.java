package bxlx.system;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableContainer;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Button extends DrawableContainer<IDrawable> implements IMouseEventListener, VisibleDrawable.VisibleDraw {
    private boolean isVisible = false;
    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final ChangeableDrawable.ChangeableValue<Consumer<Button>> atClick;
    private final ChangeableValue<Consumer<Button>> atHold;
    private final ChangeableValue<Boolean> inside;
    private final Supplier<Boolean> hover;
    private final ChangeableValue<Boolean> disabled;
    private Timer holdTimer;

    public Button(IDrawable drawable, Consumer<Button> atClick, Consumer<Button> atHold, Supplier<Boolean> disabledSuppl) {
        super(new ArrayList<>());

        Rect rect = new Rect();

        this.atClick = new ChangeableValue<>(this, atClick);
        this.atHold = new ChangeableValue<>(this, atHold);
        this.disabled = new ChangeableValue<>(this, disabledSuppl == null ? () -> false : disabledSuppl);
        this.hover = () -> lastRectangle != null &&
                rect.isContains(lastRectangle, MouseInfo.get().getPosition());
        this.inside = new ChangeableValue<>(this, () -> hover.get() && MouseInfo.get().isLeftClicked());

        add(new ColoredDrawable(rect, () -> disabled.get() ? Color.LIGHT_GRAY : inside.get() ? Color.DARK_GRAY : Color.GRAY));
        add(transformSign(drawable));

        SystemSpecific.get().setMouseEventListenerQueue(this);
    }

    @Override
    public boolean needRedraw() {
        return !isOnlyForceDraw() && (super.needRedraw() || (holdTimer != null && holdTimer.elapsed()));
    }

    public void setDrawable(IDrawable drawable) {
        get(1).setElem(transformSign(drawable));
    }

    public void setDrawable(Supplier<IDrawable> drawable) {
        get(1).setSupplier(new ValueOrSupplier<>(drawable).transform(u -> transformSign(u)));
    }

    private IDrawable transformSign(IDrawable drawable) {
        return new ClippedDrawable(new ColoredDrawable(drawable, () -> disabled.get() ? Color.WHITE : Color.BLACK),
                rectangle -> new Rectangle(
                        rectangle.getStart().add(rectangle.getSize().asPoint().multiple(1 / 16.0)),
                        rectangle.getStart().add(rectangle.getSize().asPoint().multiple(15 / 16.0))
                ));
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        boolean nowDisabled = disabled.get();
        boolean nowInside = inside.get();

        if (!nowDisabled && nowInside) {
            Consumer<Button> hold = atHold.get();
            if (hold != null && holdTimer != null && holdTimer.elapsed()) {
                holdTimer.setLength(100).setStart();
                hold.accept(this);
            }
        } else {
            holdTimer = null;
        }

        get(0).get().forceDraw(canvas);
        get(1).get().forceDraw(canvas);

        lastRectangle = canvas.getBoundingRectangle();
        isVisible = true;
    }

    public Supplier<Rectangle> getLastRectangle() {
        return () -> lastRectangle;
    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (isVisible && leftButton && hover.get() && !disabled.get()) {
            Consumer<Button> click = atClick.get();
            if (click != null) {
                click.accept(this);
            }
            holdTimer = null;
        }
    }

    @Override
    public void move(Point position) {
        if (holdTimer != null && !hover.get() || disabled.get() || !isVisible) {
            holdTimer = null;
        } else if (holdTimer == null && inside.get()) {
            Consumer<Button> hold = atHold.get();
            if (hold != null) {
                hold.accept(this);
                holdTimer = new Timer(300);
            }
        }
    }

    @Override
    public void down(Point where, boolean leftButton) {
        if (isVisible && inside.get() && !disabled.get()) {
            Consumer<Button> hold = atHold.get();
            if (hold != null) {
                hold.accept(this);
                holdTimer = new Timer(300);
            }
        }
    }

    @Override
    public void noVisibleDraw(ICanvas canvas) {
        holdTimer = null;
        lastRectangle = canvas.getBoundingRectangle();
        isVisible = false;
    }
}
