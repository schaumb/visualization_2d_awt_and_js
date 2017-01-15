package bxlx.system;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.DrawableContainer;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Button extends DrawableContainer<IDrawable> implements IMouseEventListener {
    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final ChangeableValue<Consumer<Button>> atClick;
    private final ChangeableValue<Consumer<Button>> atHold;
    private final ChangeableValue<Boolean> inside;
    private final ChangeableValue<Boolean> hover;
    private final ChangeableValue<Boolean> disabled;
    private Timer holdTimer;

    public Button(IDrawable drawable, Consumer<Button> atClick, Consumer<Button> atHold, Supplier<Boolean> disabled) {
        super(new ArrayList<>());

        Rect rect = new Rect();

        children.add(rect);
        children.add(drawable);

        this.atClick = new ChangeableValue<>(this, atClick);
        this.atHold = new ChangeableValue<>(this, atHold);
        this.disabled = new ChangeableValue<>(this, disabled);
        this.hover = new ChangeableValue<>(this, () -> lastRectangle != null &&
                rect.isContains(lastRectangle, MouseInfo.get().getPosition()));
        this.inside = new ChangeableValue<>(this, () -> hover.get() && MouseInfo.get().isLeftClicked());

        SystemSpecific.get().setMouseEventListenerQueue(this);
    }


    @Override
    public boolean needRedraw() {
        return !isOnlyForceDraw() && (super.needRedraw() || (holdTimer != null && holdTimer.elapsed()));
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        Boolean nowDisabledNPTS = disabled.get();
        boolean nowDisabled = nowDisabledNPTS != null && nowDisabledNPTS;
        boolean nowInside = inside.get();

        if (nowDisabled) {
            canvas.setColor(Color.LIGHT_GRAY);
        } else {
            if (nowInside) {
                canvas.setColor(Color.DARK_GRAY);
            } else {
                canvas.setColor(Color.GRAY);
            }
        }
        if (!nowInside) {
            holdTimer = null;
        }

        if (holdTimer != null && holdTimer.elapsed()) {
            holdTimer.setLength(100).setStart();
            atHold.get().accept(this);
        }

        children.get(0).forceDraw(canvas);

        canvas.setColor(nowDisabled ? Color.WHITE : Color.BLACK);
        canvas.clip(new Rectangle(
                canvas.getBoundingRectangle().getStart().add(canvas.getBoundingRectangle().getSize().asPoint().multiple(1 / 16.0)),
                canvas.getBoundingRectangle().getStart().add(canvas.getBoundingRectangle().getSize().asPoint().multiple(15 / 16.0))
        ));
        children.get(1).forceDraw(canvas);
        canvas.restore();

        lastRectangle = canvas.getBoundingRectangle();
    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (leftButton && hover.get() && !disabled.get()) {
            setRedraw();
            Consumer<Button> click = atClick.get();
            if (click != null) {
                click.accept(this);
            }
            holdTimer = null;
        }
    }

    @Override
    public void move(Point position) {
        if (holdTimer != null && !hover.get()) {
            holdTimer = null;
        }
    }

    @Override
    public void down(Point where, boolean leftButton) {
        if (inside.get() && !disabled.get()) {
            setRedraw();

            Consumer<Button> hold = atHold.get();
            if (hold != null) {
                hold.accept(this);
                holdTimer = new Timer(300);
            }
        }
    }
}
