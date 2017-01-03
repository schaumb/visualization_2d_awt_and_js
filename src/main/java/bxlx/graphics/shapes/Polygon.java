package bxlx.graphics.shapes;

import bxlx.graphics.Direction;
import bxlx.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Polygon extends Shape {
    private final List<Point> points;

    public Polygon(List<Point> points) {
        super(Type.POLYGON);
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public static Polygon nGon(int n, Point center, double radius, double startAngle) {
        List<Point> points = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            double radian = 2 * Math.PI * i / n + startAngle;
            points.add(
                    center.add(Direction.fromRadian(radian)
                            .getVector().multiple(radius)));
        }
        return new Polygon(points);
    }

    @Override
    public Polygon getAsPolygon() {
        return this;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        double minX = points.get(0).getX();
        double minY = points.get(0).getY();
        double maxX = minX;
        double maxY = minY;

        for (Point p : points) {
            if (minX > p.getX()) {
                minX = p.getX();
            }
            if (maxX < p.getX()) {
                maxX = p.getX();
            }
            if (minY > p.getY()) {
                minY = p.getY();
            }
            if (maxY < p.getY()) {
                maxY = p.getY();
            }
        }
        return new Rectangle(new Point(minX, minY), new Point(maxX, maxY));
    }
}
