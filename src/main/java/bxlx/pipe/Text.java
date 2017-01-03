package bxlx.pipe;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Text implements IDrawable {
    private final double XY = 2.1;
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(ICanvas canvas) {
        Rectangle rectangle = canvas.getBoundingRectangle();
        double xSize = rectangle.getSize().getWidth() * XY / text.length();
        double ySize = rectangle.getSize().getHeight();
        int size = (int) Math.min(xSize, ySize);

        canvas.setFont("sans-serif", size, false, false);
        canvas.fillText(text, rectangle.getStart().add(new Point(
                (rectangle.getSize().getWidth() - size * text.length() / XY) / 2,
                (rectangle.getSize().getHeight() + size) / 2 - size / 6.0)));
    }
}
