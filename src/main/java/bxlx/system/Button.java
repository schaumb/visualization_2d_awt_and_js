package bxlx.system;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Button implements IMouseEventListener, IDrawable {

    private Rectangle lastRectangle = Rectangle.NULL_RECTANGLE;
    private final Rect rect = new Rect();
    private final Text text;

    public Button(String text) {
        this.text = new Text(text);

        SystemSpecific.get().setMouseEventListener(this);
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public void draw(ICanvas canvas) {
        if (MouseInfo.get().isLeftClicked() &&
                rect.isContains(canvas.getBoundingRectangle(), MouseInfo.get().getPosition())) {
            canvas.setColor(Color.DARK_GRAY);
        } else {
            canvas.setColor(Color.GRAY);
        }

        rect.draw(canvas);

        canvas.setColor(Color.BLACK);
        canvas.clip(new Rectangle(
                canvas.getBoundingRectangle().getStart().add(canvas.getBoundingRectangle().getSize().asPoint().multiple(1 / 16.0)),
                canvas.getBoundingRectangle().getStart().add(canvas.getBoundingRectangle().getSize().asPoint().multiple(15 / 16.0))
        ));
        text.draw(canvas);
        canvas.restore();

        lastRectangle = canvas.getBoundingRectangle();
    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (lastRectangle != null && leftButton && rect.isContains(lastRectangle, where)) {
            onClicked();
        }
    }

    @Override
    public void move(Point position) {

    }

    @Override
    public void down(Point where, boolean leftButton) {

    }

    public void onClicked() {

    }
}
