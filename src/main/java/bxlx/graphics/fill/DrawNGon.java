package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Polygon;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNGon extends ChangeableDrawable {
    private boolean inside;
    private boolean ellipse;
    private int n;
    private double startAngle;

    public DrawNGon(boolean inside, boolean ellipse, int n, double startAngle) {
        this.inside = inside;
        this.ellipse = ellipse;
        this.n = n;
        this.startAngle = startAngle;
    }

    public DrawNGon(int n, double startAngle) {
        this(true, false, n, startAngle);
    }

    public DrawNGon(int n) {
        this(n, 0);
    }

    public int getN() {
        return n;
    }

    public DrawNGon setN(int n) {
        this.n = n;
        setRedraw();
        return this;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public DrawNGon setStartAngle(double startAngle) {
        this.startAngle = startAngle;
        setRedraw();
        return this;
    }

    public boolean isInside() {
        return inside;
    }

    public DrawNGon setInside(boolean inside) {
        this.inside = inside;
        setRedraw();
        return this;
    }

    public boolean isEllipse() {
        return ellipse;
    }

    public DrawNGon setEllipse(boolean ellipse) {
        this.ellipse = ellipse;
        setRedraw();
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        Size size = canvas.getBoundingRectangle().getSize();

        if (ellipse) {
            canvas.fill(Polygon.ellipseNGon(n, canvas.getBoundingRectangle().getCenter(), size, startAngle));
        } else {
            double radius = (inside ? size.getShorterDimension() : size.getLongerDimension()) / 2;
            canvas.fill(Polygon.nGon(n, canvas.getBoundingRectangle().getCenter(), radius, startAngle));
        }
    }
}
