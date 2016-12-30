package bxlx.graphics;

import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2016.12.23..
 */
public interface ICanvas {
    default void clearCanvas(Color to) {
        Color saved = getColor();
        setColor(to);
        fillRectangle(getBoundingRectangle());
        setColor(saved);
    }

    Rectangle getBoundingRectangle();
    void setColor(Color color);
    Color getColor();
    void fillArc(Arc arc);
    void fillRectangle(Rectangle rectangle);
    void fillPolygon(Polygon polygon);
    void drawImage(String src, Rectangle to);
    void clip(Rectangle rectangle);
    void restore();
}
