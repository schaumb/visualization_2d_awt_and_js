package bxlx.graphics.shapes;

import bxlx.graphics.Direction;
import bxlx.graphics.Point;
import bxlx.graphics.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Polygon extends Shape {
    private final List<Point> points;

    public Polygon(List<Point> points) {
        super(Type.POLYGON);
        this.points = new ArrayList<>(points);

        if(this.points.size() == 0) {
            this.points.add(Point.ORIGO);
        }
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

    public static Polygon ellipseNGon(int n, Point center, Size size, double startAngle) {
        List<Point> points = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            double radian = 2 * Math.PI * i / n + startAngle;
            points.add(
                    center.add(Direction.fromRadian(radian)
                            .getVector().multiple(size.asPoint().multiple(1 / 2.0))));
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

    @Override
    public boolean isContains(Point point) {
        List<Point> points = getPoints();
        int i, j, nvert = points.size();
        boolean c = false;

        for (i = 0, j = nvert - 1; i < nvert; j = i++) {
            if (((points.get(i).getY() >= point.getY()) != (points.get(j).getY() >= point.getY())) &&
                    (point.getX() <= (points.get(j).getX() - points.get(i).getX()) *
                            (point.getY() - points.get(i).getY()) / (points.get(j).getY() - points.get(i).getY()) +
                            points.get(i).getX())
                    )
                c = !c;
        }

        return c;
    }

    @Override
    public Polygon getTranslated(Point vector) {
        List<Point> newPoints = new ArrayList<>(points.size());

        for (Point p : points) {
            newPoints.add(p.add(vector));
        }

        return new Polygon(newPoints);
    }

    @Override
    public Polygon getScaled(double scale) {
        Point center = getBoundingRectangle().getCenter();
        List<Point> newPoints = new ArrayList<>(points.size());

        for (Point p : points) {
            newPoints.add(center.add(p.add(center.negate()).multiple(scale)));
        }

        return new Polygon(newPoints);
    }

    @Override
    public Polygon getRotated(double rotate) {
        Point center = getBoundingRectangle().getCenter();
        List<Point> newPoints = new ArrayList<>(points.size());

        for (Point p : points) {
            Point vector = p.add(center.negate());

            newPoints.add(center.add(
                    Direction.fromRadian(new Direction(vector).toRadian() + rotate)
                            .getVector().multiple(vector.length())
            ));
        }

        return new Polygon(newPoints);
    }
}
