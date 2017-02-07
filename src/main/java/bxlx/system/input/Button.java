package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.IMouseEventListener;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Button<T extends Button.Clickable> extends DrawableWrapper<T> implements IMouseEventListener, VisibleDrawable.VisibleDraw {
    public abstract static class Clickable extends ChangeableDrawable {
        protected Supplier<Boolean> inside = null;
        protected Supplier<Boolean> disabled = null;

        abstract public boolean isContains(Rectangle bound, Point position);

        public void clicked() {
        }

    }

    private boolean isVisible = false;
    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final ChangeableDrawable.ChangeableValue<Consumer<Button<T>>> atClick;
    private final ChangeableValue<Consumer<Button<T>>> atHold;
    private final ChangeableValue<Boolean> inside;
    private final Supplier<Boolean> hover;
    private final ChangeableValue<Boolean> disabled;
    private Timer holdTimer;

    public Button(T clickable, Consumer<Button<T>> atClick, Consumer<Button<T>> atHold, Supplier<Boolean> disabledSuppl) {
        super(clickable);
        MouseInfo.get();

        this.atClick = new ChangeableValue<>(this, atClick);
        this.atHold = new ChangeableValue<>(this, atHold);
        this.disabled = new ChangeableValue<>(this, disabledSuppl == null ? () -> false : disabledSuppl);
        this.hover = () -> lastRectangle != null && lastRectangle.isContains(MouseInfo.get().getPosition()) &&
                getClickableChild().isContains(lastRectangle, MouseInfo.get().getPosition());
        this.inside = new ChangeableValue<>(this, () -> MouseInfo.get().isLeftClicked() && hover.get());
        clickable.inside = inside.getAsSupplier();
        clickable.disabled = disabled.getAsSupplier();

        SystemSpecific.get().setMouseEventListenerQueue(this);
    }

    private T getClickableChild() {
        T c = getChild().get();
        if (c.inside == null) {
            c.inside = inside.getAsSupplier();
            c.disabled = disabled.getAsSupplier();
        }
        return c;
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().setIf(holdTimer != null && holdTimer.elapsed(), Redraw.I_NEED_REDRAW);
    }

    public boolean isInsideNow() {
        return inside.get();
    }

    public ChangeableValue<Boolean> getDisabled() {
        return disabled;
    }

    public ChangeableValue<Consumer<Button<T>>> getAtClick() {
        return atClick;
    }

    public ChangeableValue<Consumer<Button<T>>> getAtHold() {
        return atHold;
    }

    @Override
    protected boolean parentRedrawSatisfy() {
        return true;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        Redraw redraw = needRedraw();
        boolean nowDisabled = disabled.get();
        boolean nowInside = inside.get();

        if ((!nowDisabled && inside.isChanged()) || disabled.isChanged() || isManuallyRedraw() || redraw.noNeedRedraw() || redraw.childNeedRedraw()) {
            getClickableChild().forceDraw(canvas);
        }

        lastRectangle = canvas.getBoundingRectangle();
        isVisible = true;

        if (!nowDisabled && nowInside) {
            Consumer<Button<T>> hold = atHold.get();
            if (hold != null && holdTimer != null && holdTimer.elapsed()) {
                holdTimer.setPercent(holdTimer.overPercent()).setStart();
                hold.accept(this);
            }
        } else {
            holdTimer = null;
        }
    }

    public Supplier<Rectangle> getLastRectangle() {
        return () -> lastRectangle;
    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (isVisible && leftButton && hover.get() && !disabled.get()) {
            getClickableChild().clicked();
            Consumer<Button<T>> click = atClick.get();
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
            Consumer<Button<T>> hold = atHold.get();
            if (hold != null) {
                hold.accept(this);
                holdTimer = new Timer(300);
            }
        }
    }

    @Override
    public void down(Point where, boolean leftButton) {
        if (isVisible && inside.get() && !disabled.get()) {
            Consumer<Button<T>> hold = atHold.get();
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
