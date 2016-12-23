package bxlx.graphics.shapes;

import bxlx.graphics.Point;
import bxlx.graphics.Size;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Rectangle {
    private final Point start;
    private final Size size;

    public Rectangle(double x, double y, double width, double height) {
        this(new Point(x, y), new Size(width, height));
    }

    public Rectangle(Point start, Size size) {
        this.start = start;
        this.size = size;
    }

    public Rectangle withStart(Point s) {
        return new Rectangle(s, size);
    }

    public Rectangle withSize(Size s) {
        return new Rectangle(start, s);
    }

    public Point getStart() {
        return start;
    }

    public Size getSize() {
        return size;
    }

    public Point getCenter() {
        return start.add(getSize().asPoint().multiple(0.5));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (!start.equals(rectangle.start)) return false;
        return size.equals(rectangle.size);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + size.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "start=" + start +
                ", size=" + size +
                '}';
    }
}
