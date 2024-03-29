package bxlx.graphics;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Size {
    public static final Size ZERO = square(0);
    private final double width;
    private final double height;

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public static Size square(double size) {
        return new Size(size, size);
    }

    public Point asPoint() {
        return new Point(width, height);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getShorterDimension() {
        return Math.min(width, height);
    }

    public double getLongerDimension() {
        return Math.max(width, height);
    }

    public Size withWidth(double width) {
        return new Size(width, height);
    }

    public Size withHeight(double height) {
        return new Size(width, height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Size size = (Size) o;

        return asPoint().equals(size.asPoint());
    }

    @Override
    public int hashCode() {
        return asPoint().hashCode();
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    public double getRatio() {
        return getWidth() == 0 ? 1 : getHeight() / getWidth();
    }
}
