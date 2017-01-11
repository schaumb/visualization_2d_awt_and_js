package bxlx.graphics.shapes;

import bxlx.graphics.Direction;
import bxlx.graphics.Point;

import java.util.ArrayList;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Arc extends Shape {
    private final Point center;
    private final double radius;
    private final double fromRadian;
    private final double toRadian;

    public Arc(Point center, double radius, double fromRadian, double toRadian) {
        super(Type.ARC);
        if (fromRadian == toRadian)
            radius = 0;
        this.center = center;
        this.radius = radius;
        this.fromRadian = fromRadian;
        this.toRadian = toRadian;
    }

    public static Arc circle(Point center, double radius) {
        return new Arc(center, radius, 0, 2 * Math.PI);
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public double getFromRadian() {
        return fromRadian;
    }

    public double getToRadian() {
        return toRadian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arc arc = (Arc) o;

        if (arc.radius != radius) return false;
        if (arc.fromRadian != fromRadian) return false;
        if (arc.toRadian != toRadian) return false;
        return center.equals(arc.center);
    }

    @Override
    public int hashCode() {
        int result = center.hashCode();
        result = 31 * result + (int) (radius * 1000);
        result = 31 * result + (int) (fromRadian * 1000);
        result = 31 * result + (int) (toRadian * 1000);
        return result;
    }

    @Override
    public String toString() {
        return "Arc{" +
                "center=" + center +
                ", radius=" + radius +
                ", fromRadian=" + fromRadian +
                ", toRadian=" + toRadian +
                '}';
    }

    @Override
    public Arc getAsArc() {
        return this;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        ArrayList<Point> points = new ArrayList<>(7);
        points.add(center);
        points.add(center.add(Direction.fromRadian(fromRadian).getVector().multiple(radius)));
        points.add(center.add(Direction.fromRadian(toRadian).getVector().multiple(radius)));

        for (double d = fromRadian - fromRadian % (Math.PI / 2) + (Math.PI / 2); d < toRadian; d += Math.PI / 2) {
            points.add(center.add(Direction.fromRadian(d).getVector().multiple(radius)));
        }

        for (double d = fromRadian - fromRadian % (Math.PI / 2); d > toRadian; d -= Math.PI / 2) {
            points.add(center.add(Direction.fromRadian(d).getVector().multiple(radius)));
        }

        return new Polygon(points).getBoundingRectangle();
    }

    @Override
    public boolean isContains(Point point) {
        Point vector = point.add(center.negate());

        if (vector.length() > radius)
            return false;

        double i = new Direction(vector).toRadian();
        while (fromRadian > i) {
            i += 2 * Math.PI;
        }

        if (fromRadian <= i && i <= toRadian) {
            return true;
        }

        i -= 2 * Math.PI;

        return toRadian <= i && i < fromRadian;
    }

    @Override
    public Shape getTranslated(Point vector) {
        return new Arc(center.add(vector), radius, fromRadian, toRadian);
    }

    @Override
    public Shape getScaled(double scale) {
        return new Arc(center, radius * scale, fromRadian, toRadian);
    }

    @Override
    public Shape getRotated(double rotate) {
        return new Arc(center, radius, fromRadian + rotate, toRadian + rotate);
    }
}
