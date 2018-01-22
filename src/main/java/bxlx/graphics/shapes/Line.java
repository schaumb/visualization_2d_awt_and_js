package bxlx.graphics.shapes;

import bxlx.graphics.Point;

public class Line {
    private final Point from, to;

    public Line(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }
}
