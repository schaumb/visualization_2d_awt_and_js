package bxlx.graphics;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point same(double coord) {
        return new Point(coord, coord);
    }

    public Size asSize() {
        return new Size(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    public Point add(double n) {
        return new Point(x + n, y + n);
    }

    public Point multiple(Point other) {
        return new Point(x * other.x, y * other.y);
    }

    public Point multiple(double n) {
        return new Point(x * n, y * n);
    }

    public Point negate() {
        return new Point(-x, -y);
    }

    public Point abs() {
        return new Point(x < 0 ? -x : x,
                y < 0 ? -y : y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Point norm() {
        return multiple(1 / length());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (point.x != x) return false;
        return point.y != y;
    }

    @Override
    public int hashCode() {
        int result = (int) (x * 1000);
        result = 31 * result + (int) (y * 1000);
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
