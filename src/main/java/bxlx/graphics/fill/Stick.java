package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.drawable.DrawableContainer;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.SystemSpecific;

import java.util.Arrays;

/**
 * Created by qqcs on 2017.01.10..
 */
public class Stick extends DrawableContainer {
    private double angle;
    private double thickness;

    public Stick(double angle, double thickness, IDrawable start, IDrawable end) {
        super(Arrays.asList(start, end));
        this.angle = angle;
        this.thickness = thickness;
    }

    public double getAngle() {
        return angle;
    }

    public Stick setAngle(double angle) {
        this.angle = angle;
        setRedraw();
        return this;
    }

    public double getThickness() {
        return thickness;
    }

    public Stick setThickness(double thickness) {
        this.thickness = thickness;
        setRedraw();
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        boolean forcedRedraw = !needRedraw() || iChanged();

        Rectangle bound = canvas.getBoundingRectangle();
        Point center = bound.getCenter();
        Size size = bound.getSize();

        double referenceRad = new Direction(Direction.fromRadian(angle).getVector().abs()).toRadian();
        double sizeRad = new Direction(size.getWidth(), size.getHeight()).toRadian();

        double stickSize = referenceRad > sizeRad ? size.getWidth() / Math.cos(angle) : size.getHeight() / Math.sin(angle);
        stickSize = Math.abs(stickSize);

        Point halfStick = Direction.fromRadian(angle).getVector().multiple(stickSize / 4);

        Point halfStickThick = new Point(-halfStick.getY(), halfStick.getX()).norm().multiple(thickness / 2);

        if (forcedRedraw) {
            canvas.fill(new Polygon(Arrays.asList(
                    center.add(halfStick).add(halfStickThick),
                    center.add(halfStick).add(halfStickThick.negate()),
                    center.add(halfStick.negate()).add(halfStickThick.negate()),
                    center.add(halfStick.negate()).add(halfStickThick)
            )));
        }

        if(children.get(0) == null && forcedRedraw) {
            canvas.fill(Arc.circle(center.add(halfStick.negate()), thickness / 2));
        } else if(children.get(0) != null) {
            canvas.clip(new Polygon(Arrays.asList(
                    center,
                    center.add(halfStick.negate().multiple(2)),
                    center.add(halfStick.negate()).add(halfStickThick.multiple(3.0)),
                    center.add(halfStick.negate()).add(halfStickThick.multiple(-3.0))
            )).getBoundingRectangle());

            if(forcedRedraw) {
                children.get(0).forceDraw(canvas);
            } else {
                children.get(0).draw(canvas);
            }
            canvas.restore();
        }

        if(children.get(1) == null && forcedRedraw) {
            canvas.fill(Arc.circle(center.add(halfStick), thickness / 2));
        } else if(children.get(1) != null) {
            canvas.clip(new Polygon(Arrays.asList(
                    center,
                    center.add(halfStick.multiple(2)),
                    center.add(halfStick).add(halfStickThick.multiple(3.0)),
                    center.add(halfStick).add(halfStickThick.multiple(-3.0))
            )).getBoundingRectangle());

            if(forcedRedraw) {
                children.get(1).forceDraw(canvas);
            } else {
                children.get(1).draw(canvas);
            }
            canvas.restore();
        }
    }
}
