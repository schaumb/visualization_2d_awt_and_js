package bxlx.graphics.fill;

import bxlx.graphics.Drawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Shape;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNGon extends Drawable {
    private final boolean inside;
    private final boolean ellipse;
    private final int n;
    private final double startAngle;
    private final boolean makeCentered;

    public DrawNGon(boolean inside, boolean ellipse, int n, double startAngle, boolean makeCentered) {
        this.inside = inside;
        this.ellipse = ellipse;
        this.n = n;
        this.startAngle = startAngle;
        this.makeCentered = makeCentered;
    }

    public DrawNGon(int n, double startAngle, boolean makeCentered) {
        this(true, false, n, startAngle, makeCentered);
    }

    public DrawNGon(int n, double startAngle) {
        this(n, startAngle, false);
    }

    public DrawNGon(int n) {
        this(n, 0);
    }

    @Override
    protected void forceDraw(ICanvas canvas) {
        Size size = canvas.getBoundingRectangle().getSize();
        Point center = canvas.getBoundingRectangle().getCenter();

        Shape polygon;
        if (ellipse) {
            polygon = Polygon.ellipseNGon(n, center, size, startAngle);
        } else {
            double radius = (inside ? size.getShorterDimension() : size.getLongerDimension()) / 2;
            polygon = Polygon.nGon(n, center, radius, startAngle);
        }

        if (makeCentered) {
            Point nowCenter = polygon.getBoundingRectangle().getCenter();
            polygon = polygon.getTranslated(center.add(nowCenter.negate()));
        }

        canvas.fill(polygon);
    }
}
