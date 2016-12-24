package bxlx.graphics;

import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2016.12.23..
 */
public interface ICanvas {
    default void clearCanvas() {
        setColor(Color.WHITE);
        fillRectangle(getBoundingRectangle());
    }

    Rectangle getBoundingRectangle();
    void setColor(Color color);
    void fillArc(Arc arc);
    void fillRectangle(Rectangle rectangle);
    void fillPolygon(Polygon polygon);
    void drawImage(String src, Rectangle to);
    void clip(Rectangle rectangle);
    void restore();
}
