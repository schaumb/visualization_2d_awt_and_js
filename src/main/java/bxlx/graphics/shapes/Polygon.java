package bxlx.graphics.shapes;

import bxlx.SystemSpecific;
import bxlx.graphics.Direction;
import bxlx.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Polygon {
    private final List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public static Polygon nGon(int n, Point center, double radius, double startAngle) {
        List<Point> points = new ArrayList<>(n);
        for(int i = 0; i < n; ++i) {
            double radian = 2 * Math.PI * i / n + startAngle;
            points.add(
                    center.add(Direction.fromRadian(radian)
                        .getVector().multiple(radius)));
        }
        return new Polygon(points);
    }
}
