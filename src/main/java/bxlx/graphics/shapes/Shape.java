package bxlx.graphics.shapes;

/**
 * Created by qqcs on 2017.01.03..
 */
public abstract class Shape {
    public enum Type {ARC, POLYGON, RECTANGLE}

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
}
