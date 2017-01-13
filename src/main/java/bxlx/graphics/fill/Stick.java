package bxlx.graphics.fill;

import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.drawable.DrawableContainer;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;

import java.util.Arrays;

/**
 * Created by qqcs on 2017.01.10..
 */
public class Stick extends DrawableContainer<IDrawable> {
    private double angle;
    private double length;
    private double thickness;

    public Stick(double angle, double length, double thickness, IDrawable start, IDrawable end) {
        super(Arrays.asList(start, end));
        this.angle = angle;
        this.length = length;
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

    public double getLength() {
        return length;
    }

    public Stick setLength(double length) {
        this.length = length;
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

        double sizeRad = new Direction(size.asPoint()).toRadian();

        double diagonalAngle1 = angle + Math.atan(length);
        double diagonalAngle2 = -angle + Math.atan(length);
        double diagonalReferenceRad1 = new Direction(Direction.fromRadian(diagonalAngle1).getVector().abs()).toRadian();
        double diagonalReferenceRad2 = new Direction(Direction.fromRadian(diagonalAngle2).getVector().abs()).toRadian();

        double stickDiagonalSize1 = diagonalReferenceRad1 > sizeRad ? size.getWidth() / Math.cos(diagonalAngle1) :
                size.getHeight() / Math.sin(diagonalAngle1);

        double stickDiagonalSize2 = diagonalReferenceRad2 > sizeRad ? size.getWidth() / Math.cos(diagonalAngle2) :
                size.getHeight() / Math.sin(diagonalAngle2);

        double stickDiagonalSize = Math.min(Math.abs(stickDiagonalSize1), Math.abs(stickDiagonalSize2));
        double stickSize = stickDiagonalSize / Math.sqrt(1 + length * length);
        double stickThick = stickSize * length;

        Point stickVector = Direction.fromRadian(angle).getVector().norm();
        Point stickThickVector = new Point(-stickVector.getY(), stickVector.getX()).norm();


        if (forcedRedraw) {
            canvas.fill(new Polygon(Arrays.asList(
                    center.add(stickVector.multiple(stickSize / 2 - stickThick * 2 / 3)).add(stickThickVector.multiple(stickThick * thickness / 2)),
                    center.add(stickVector.multiple(stickSize / 2 - stickThick * 2 / 3)).add(stickThickVector.multiple(-stickThick * thickness / 2)),
                    center.add(stickVector.multiple(stickThick * 2 / 3 - stickSize / 2)).add(stickThickVector.multiple(-stickThick * thickness / 2)),
                    center.add(stickVector.multiple(stickThick * 2 / 3 - stickSize / 2)).add(stickThickVector.multiple(stickThick * thickness / 2))
            )));
        }

        if (children.get(0) == null && forcedRedraw) {
            canvas.fill(Arc.circle(center.add(stickVector.multiple(stickThick * 2 / 3 - stickSize / 2)), stickThick * thickness / 2));
        } else if (children.get(0) != null) {
            Polygon p = new Polygon(Arrays.asList(
                    center.add(stickVector.multiple(-stickSize / 2)).add(stickThickVector.multiple(stickThick / 2)),
                    center.add(stickVector.multiple(-stickSize / 2)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(-stickSize / 2 + stickThick)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(-stickSize / 2 + stickThick)).add(stickThickVector.multiple(stickThick / 2))
            ));
            canvas.clip(p.getBoundingRectangle());

            if (forcedRedraw) {
                children.get(0).forceDraw(canvas);
            } else {
                children.get(0).draw(canvas);
            }
            canvas.restore();
        }


        if (children.get(1) == null && forcedRedraw) {
            canvas.fill(Arc.circle(center.add(stickVector.multiple(stickSize / 2 - stickThick * 2 / 3)), stickThick * thickness / 2));
        } else if (children.get(1) != null) {
            Polygon p = new Polygon(Arrays.asList(
                    center.add(stickVector.multiple(stickSize / 2)).add(stickThickVector.multiple(stickThick / 2)),
                    center.add(stickVector.multiple(stickSize / 2)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(stickSize / 2 - stickThick)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(stickSize / 2 - stickThick)).add(stickThickVector.multiple(stickThick / 2))
            ));
            canvas.clip(p.getBoundingRectangle());

            if (forcedRedraw) {
                children.get(1).forceDraw(canvas);
            } else {
                children.get(1).draw(canvas);
            }
            canvas.restore();
        }
    }
}
