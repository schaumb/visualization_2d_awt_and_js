package bxlx.graphics.fill;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Text implements IDrawable {
    private final double XY = 1;
    private String text;
    private Color color;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(ICanvas canvas) {
        if(text.isEmpty())
            return;

        Rectangle rectangle = canvas.getBoundingRectangle();
        double xSize = rectangle.getSize().getWidth() * XY / text.length();
        double ySize = rectangle.getSize().getHeight();
        int size = (int) Math.min(xSize, ySize);

        if (color != null) {
            canvas.setColor(color);
        }
        canvas.setFont("sans-serif", size, false, false);
        canvas.fillText(text, rectangle.getStart().add(new Point(
                (rectangle.getSize().getWidth() - size * text.length() / XY) / 2,
                (rectangle.getSize().getHeight() + size) / 2 - size / 6.0)));
    }
}
