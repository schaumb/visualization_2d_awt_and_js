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

    public Button(String text) {
        this.text = new Text(text);
        setRedraw();
        SystemSpecific.get().setMouseEventListener(this);
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text = new Text(text);
        setRedraw();
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (wasInside = (MouseInfo.get().isLeftClicked() &&
                rect.isContains(canvas.getBoundingRectangle(), MouseInfo.get().getPosition()))) {
            canvas.setColor(Color.DARK_GRAY);
        } else {
            canvas.setColor(Color.GRAY);
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

    @Override
    public void up(Point where, boolean leftButton) {
        if (lastRectangle != null && leftButton && rect.isContains(lastRectangle, where)) {
            setRedraw();
            onClicked();
        }
    }

    @Override
    public void move(Point position) {
        if (lastRectangle != null && wasInside ^ (MouseInfo.get().isLeftClicked() &&
                rect.isContains(lastRectangle, position))) {
            setRedraw();
        }
    }

    @Override
    public void down(Point where, boolean leftButton) {
        if (lastRectangle != null && leftButton && rect.isContains(lastRectangle, where)) {
            setRedraw();
        }
    }

    public void onClicked() {

    }
}
