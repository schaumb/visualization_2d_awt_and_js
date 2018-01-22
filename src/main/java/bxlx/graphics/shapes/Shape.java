package bxlx.graphics.shapes;

import bxlx.graphics.Point;

/**
 * Created by qqcs on 2017.01.03..
 */
public abstract class Shape {
    private final Type type;

    protected Shape(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Arc getAsArc() {
        return null;
    }

    public Polygon getAsPolygon() {
        return null;
    }

    public Rectangle getAsRectangle() {
        return null;
    }

    public abstract Rectangle getBoundingRectangle();

    public abstract boolean isContains(Point point);

    public abstract Shape getTranslated(Point vector);

    public abstract Shape getScaled(double scale);

    public abstract Shape getRotated(double rotate);

    public enum Type {ARC, POLYGON, RECTANGLE}
}
