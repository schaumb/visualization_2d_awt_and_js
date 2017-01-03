package bxlx.system;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.pipe.Rect;
import bxlx.pipe.Text;

/**
 * Created by qqcs on 2017.01.03..
 */
public abstract class Button implements IMouseEventListener, IDrawable {

    private final Rectangle rectangle;
    private final Rect rect = new Rect();
    private final Text text;

    public Button(Rectangle rectangle, String text) {
        this.rectangle = rectangle;
        this.text = new Text(text);
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public void draw(ICanvas canvas) {

        canvas.clip(rectangle);

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

        canvas.restore();


    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (leftButton && rect.isContains(rectangle, where)) {
            onClicked();
        }
    }

    @Override
    public void move(Point position) {

    }

    @Override
    public void down(Point where, boolean leftButton) {

    }

    public abstract void onClicked();
}
