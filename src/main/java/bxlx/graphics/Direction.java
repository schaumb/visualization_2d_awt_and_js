package bxlx.graphics;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Direction {
    private final Point vector;

    public Direction(double x, double y) {
        this(new Point(x, y));
    }

    public Direction(Point vector) {
        this.vector = vector.norm();
    }

    public Point getVector() {
        return vector;
    }

    public static final Direction UP = new Direction(0, -1);
    public static final Direction DOWN = new Direction(0, 1);
    public static final Direction LEFT = new Direction(-1, 0);
    public static final Direction RIGHT = new Direction(1, 0);

    public static Direction[] values() {
        return new Direction[] {
                UP, LEFT, DOWN, RIGHT
        };
    }
    public static Direction fromRadian(double radian) {
        return new Direction(Math.cos(radian), -Math.sin(radian));
    }

    public double toRadian() {
        return Math.atan2(-vector.getY(), vector.getX());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Direction direction = (Direction) o;

        return vector.equals(direction.vector);
    }

    @Override
    public int hashCode() {
        return vector.hashCode();
    }

    @Override
    public String toString() {
        return "Direction{" +
                "vector=" + vector +
                '}';
    }

    public Direction opposite() {
        if(this == UP)
            return DOWN;
        if(this == DOWN)
            return UP;
        if(this == LEFT)
            return RIGHT;
        if(this == RIGHT)
            return LEFT;

        return new Direction(-vector.getX(), -vector.getY());
    }
}
