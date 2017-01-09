package bxlx.system;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Button extends ChangeableDrawable implements IMouseEventListener {

    private boolean wasInside = false;
    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final Rect rect = new Rect();
    private Text text;
    private Runnable atClick;
    private Runnable atHold;
    private Timer holdTimer;


    public Button(String text, Runnable atClick, Runnable atHold) {
        this(new Text(text), atClick, atHold);
    }

    public Button(Text text, Runnable atClick, Runnable atHold) {
        this.text = text;
        this.atClick = atClick;
        this.atHold = atHold;
        setRedraw();
        SystemSpecific.get().setMouseEventListenerQueue(this);
    }


    @Override
    public boolean needRedraw() {
        return super.needRedraw() || text.needRedraw() || (holdTimer != null && holdTimer.elapsed());
    }

    @Override
    public void forceRedraw(ICanvas canvas) {

        if (wasInside = isInside(canvas.getBoundingRectangle(), MouseInfo.get().getPosition(), MouseInfo.get().isLeftClicked())) {
            canvas.setColor(Color.DARK_GRAY);
        } else {
            canvas.setColor(Color.GRAY);
        }
        if (!wasInside) {
            holdTimer = null;
        }

        if (holdTimer != null && holdTimer.elapsed()) {
            holdTimer.setLength(100).setStart();
            atHold.run();
        }

        rect.forceDraw(canvas);

        canvas.setColor(Color.BLACK);
        canvas.clip(new Rectangle(
                canvas.getBoundingRectangle().getStart().add(canvas.getBoundingRectangle().getSize().asPoint().multiple(1 / 16.0)),
                canvas.getBoundingRectangle().getStart().add(canvas.getBoundingRectangle().getSize().asPoint().multiple(15 / 16.0))
        ));
        text.forceDraw(canvas);
        canvas.restore();

        lastRectangle = canvas.getBoundingRectangle();
    }

    private boolean isInside(Rectangle rectangle, Point where, boolean leftButton) {
        return rectangle != null && leftButton && rect.isContains(rectangle, where);
    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (isInside(lastRectangle, where, leftButton)) {
            setRedraw();
            if (atClick != null) {
                atClick.run();
            }
            holdTimer = null;
        }
    }

    @Override
    public void move(Point position) {
        boolean inside = isInside(lastRectangle, position, MouseInfo.get().isLeftClicked());
        if (wasInside ^ inside) {
            setRedraw();
        }
        if (!inside) {
            holdTimer = null;
        }
    }

    @Override
    public void down(Point where, boolean leftButton) {
        if (isInside(lastRectangle, where, leftButton)) {
            setRedraw();
            if (atHold != null) {
                atHold.run();
                holdTimer = new Timer(300);
            }
        }
    }
}
