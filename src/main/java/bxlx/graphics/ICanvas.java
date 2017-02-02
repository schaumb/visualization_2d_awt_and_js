package bxlx.graphics;

import bxlx.graphics.shapes.Rectangle;
import bxlx.graphics.shapes.Shape;

/**
 * Created by qqcs on 2016.12.23..
 */
public interface ICanvas {
    default void clearCanvas(Color to) {
        Color saved = getColor();
        setColor(to);
        fill(getBoundingRectangle());
        setColor(saved);
    }

    Rectangle getBoundingRectangle();

    void setColor(Color color);

    Color getColor();

    void fill(Shape shape);

    void drawImage(String src, Rectangle to);

    void setFont(Font font);

    Font getFont();

    void fillText(String text, Point to);

    int textWidth(String text);

    void clip(Shape shape);

    void clipInverse(Shape shape);

    void restore();

    void fakeClip(Rectangle rectangle);

    void fakeRestore();
}
