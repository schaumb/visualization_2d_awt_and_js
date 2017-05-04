package bxlx.graphics.shapes;

import bxlx.graphics.Point;
import bxlx.graphics.Size;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Rectangle extends Shape {
    private final Point start;
    private final Size size;

    public Rectangle(double x, double y, double width, double height) {
        this(new Point(x, y), new Size(width, height));
    }

    public Rectangle(Point start, Point end) {
        this(start, end.add(start.negate()).asSize());
    }

    public Rectangle(Point start, Size size) {
        super(Type.RECTANGLE);
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

    public Point getEnd() {
        return start.add(getSize().asPoint());
    }

    public Rectangle intersect(Rectangle other) {
        double xStart = Math.max(getStart().getX(), other.getStart().getX());
        double yStart = Math.max(getStart().getY(), other.getStart().getY());
        double xEnd = Math.min(getEnd().getX(), other.getEnd().getX());
        double yEnd = Math.min(getEnd().getY(), other.getEnd().getY());

        if (xEnd <= xStart || yEnd <= yStart) return NULL_RECTANGLE;

        return new Rectangle(xStart, yStart, xEnd - xStart, yEnd - yStart);
    }

    public static final Rectangle NULL_RECTANGLE = new Rectangle(0, 0, 0, 0);

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

    @Override
    public Rectangle getAsRectangle() {
        return this;
    }

    public Rectangle getBoundingRectangle() {
        return this;
    }

    @Override
    public boolean isContains(Point point) {
        return start.getX() <= point.getX() && point.getX() <= start.getX() + size.getWidth()
                && start.getY() <= point.getY() && point.getY() <= start.getY() + size.getHeight();
    }

    @Override
    public Rectangle getTranslated(Point vector) {
        return withStart(start.add(vector));
    }

    @Override
    public Rectangle getScaled(double scale) {
        Point center = getCenter();
        return new Rectangle(center.add(start.add(center.negate()).multiple(scale)), size.asPoint().multiple(scale).asSize());
    }

    @Override
    public Rectangle getRotated(double rotate) {
        if ((rotate + 7 * Math.PI / 4) % Math.PI > Math.PI / 2) {
            return this;
        }
        Rectangle r = withSize(new Size(size.getHeight(), size.getWidth()));
        return r.getTranslated(getCenter().add(r.getCenter().negate()));
    }
}
