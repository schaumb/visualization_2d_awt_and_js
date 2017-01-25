package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.IMouseEventListener;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Button extends DrawableWrapper<Button.Clickable> implements IMouseEventListener, VisibleDrawable.VisibleDraw {
    public abstract static class Clickable implements IDrawable {
        protected Supplier<Boolean> inside = null;
        protected Supplier<Boolean> disabled = null;

        abstract public boolean isContains(Rectangle bound, Point position);
    }

    public static class RectClickable extends Clickable {
        private final Rect rect = new Rect();
        private final IDrawable drawable;

        private IDrawable transformSign(IDrawable drawable) {
            if (drawable == null) return null;
            return new ClippedDrawable<>(new ColoredDrawable<>(drawable, () -> disabled.get() ? Color.WHITE : Color.BLACK), false,
                    rectangle -> new Rectangle(
                            rectangle.getStart().add(rectangle.getSize().asPoint().multiple(1 / 16.0)),
                            rectangle.getStart().add(rectangle.getSize().asPoint().multiple(15 / 16.0))
                    ));
        }

        public RectClickable(IDrawable drawable) {
            this.drawable = transformSign(drawable);
        }

        @Override
        public Redraw needRedraw() {
            return drawable == null ? new Redraw() : drawable.needRedraw();
        }

        @Override
        public void forceDraw(ICanvas canvas) {
            canvas.setColor(disabled.get() ? Color.LIGHT_GRAY : inside.get() ? Color.DARK_GRAY : Color.GRAY);
            rect.forceDraw(canvas);
            if (drawable != null) {
                drawable.forceDraw(canvas);
            }
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return rect.isContains(bound, position);
        }
    }

    public static class ChangeImgClickable extends Clickable {
        private final DrawImage disabledImg;
        private final DrawImage insideImg;
        private final DrawImage normalImg;
        private final Predicate<Color> clickable;

        public ChangeImgClickable(String norm, String ins, String dis, Predicate<Color> clickable) {
            this.disabledImg = new DrawImage(dis);
            this.insideImg = new DrawImage(ins);
            this.normalImg = new DrawImage(norm);
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
            } else {
                return normalImg;
            }
        }

        @Override
        public void forceDraw(ICanvas canvas) {
            getActualImage().forceDraw(canvas);
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return clickable.test(normalImg.getColor(bound, position));
        }
    }

    public static class SameImgClickable extends Clickable {
        private final DrawImage img;
        private final UnaryOperator<Rectangle> normalClip;
        private final UnaryOperator<Rectangle> insideClip;
        private final UnaryOperator<Rectangle> disableClip;
        private Rectangle lastRectangle;
        private final Predicate<Color> clickable;

        public SameImgClickable(DrawImage img, UnaryOperator<Rectangle> normalClip, UnaryOperator<Rectangle> insideClip, UnaryOperator<Rectangle> disableClip, Predicate<Color> clickable) {
            this.img = img;
            this.normalClip = normalClip;
            this.insideClip = insideClip;
            this.disableClip = disableClip;
            this.clickable = clickable;
        }

        private UnaryOperator<Rectangle> getActualClip() {
            if (disabled.get()) {
                return disableClip;
            } else if (inside.get()) {
                return insideClip;
            } else {
                return normalClip;
            }
        }

        @Override
        public Redraw needRedraw() {
            return new Redraw();
        }

        @Override
        public void forceDraw(ICanvas canvas) {
            Rectangle clipRect = getActualClip().apply(canvas.getBoundingRectangle());

            canvas.fakeClip(lastRectangle = clipRect);
            img.forceDraw(canvas);
            canvas.fakeRestore();
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return lastRectangle != null && clickable.test(img.getColor(lastRectangle, position));
        }
    }

    private boolean isVisible = false;
    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final ChangeableDrawable.ChangeableValue<Consumer<Button>> atClick;
    private final ChangeableValue<Consumer<Button>> atHold;
    private final ChangeableValue<Boolean> inside;
    private final Supplier<Boolean> hover;
    private final ChangeableValue<Boolean> disabled;
    private Timer holdTimer;

    public Button(IDrawable drawable) {
        this(new RectClickable(drawable), null, null, null);
    }

    public Button(Clickable clickable, Consumer<Button> atClick, Consumer<Button> atHold, Supplier<Boolean> disabledSuppl) {
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

    private Clickable getClickableChild() {
        Clickable c = getChild().get();
        if (c.inside == null) {
            c.inside = inside.getAsSupplier();
            c.disabled = disabled.getAsSupplier();
        }
        return c;
    }

    @Override
    public Redraw needRedraw() {
        return super.needRedraw().setIf(holdTimer != null && holdTimer.elapsed(), Redraw.I_NEED_REDRAW);
    }

    public boolean isInsideNow() {
        return inside.get();
    }

    public ChangeableValue<Boolean> getDisabled() {
        return disabled;
    }

    public ChangeableValue<Consumer<Button>> getAtClick() {
        return atClick;
    }

    public ChangeableValue<Consumer<Button>> getAtHold() {
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

        if ((!nowDisabled && inside.isChanged()) || disabled.isChanged() || redraw.noNeedRedraw() || redraw.childNeedRedraw()) {
            getClickableChild().forceDraw(canvas);
        }

        lastRectangle = canvas.getBoundingRectangle();
        isVisible = true;

        if (!nowDisabled && nowInside) {
            Consumer<Button> hold = atHold.get();
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
