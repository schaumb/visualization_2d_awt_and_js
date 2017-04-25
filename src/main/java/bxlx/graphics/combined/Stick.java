package bxlx.graphics.combined;

import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.container.DrawableContainer;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;

import java.util.Arrays;

/**
 * Created by qqcs on 2017.01.10..
 */
public class Stick extends DrawableContainer<IDrawable> {
    private final ChangeableValue<Double> angle;
    private final ChangeableDependentValue<Double, Rectangle> length;
    private final ChangeableValue<Double> thickness;

    public Stick(double angle, double length, double thickness, IDrawable start, IDrawable end) {
        super(Arrays.asList(start, end));
        this.angle = new ChangeableValue<>(this, angle);
        this.length = new ChangeableDependentValue<>(this, r -> length);
        this.thickness = new ChangeableValue<>(this, thickness);

    }

    public ChangeableValue<Double> getAngle() {
        return angle;
    }

    public ChangeableDependentValue<Double, Rectangle> getLength() {
        return length;
    }

    public ChangeableValue<Double> getThickness() {
        return thickness;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        Rectangle bound = canvas.getBoundingRectangle();
        Point center = bound.getCenter();
        Size size = bound.getSize();

        double nowAngle = angle.get();
        double nowLength = length.setDep(bound).get();
        double nowThickness = thickness.get();

        Redraw redraw = needRedraw();
        boolean noNeedRedraw = redraw.noNeedRedraw();
        boolean iNeedRedraw = redraw.iNeedRedraw() || redraw.parentNeedRedraw();

        double sizeRad = new Direction(size.asPoint()).toRadian();

        double diagonalAngle1 = nowAngle + Math.atan(nowLength);
        double diagonalAngle2 = -nowAngle + Math.atan(nowLength);
        double diagonalReferenceRad1 = new Direction(Direction.fromRadian(diagonalAngle1).getVector().abs()).toRadian();
        double diagonalReferenceRad2 = new Direction(Direction.fromRadian(diagonalAngle2).getVector().abs()).toRadian();

        double stickDiagonalSize1 = diagonalReferenceRad1 > sizeRad ? size.getWidth() / Math.cos(diagonalAngle1) :
                size.getHeight() / Math.sin(diagonalAngle1);

        double stickDiagonalSize2 = diagonalReferenceRad2 > sizeRad ? size.getWidth() / Math.cos(diagonalAngle2) :
                size.getHeight() / Math.sin(diagonalAngle2);

        double stickDiagonalSize = Math.min(Math.abs(stickDiagonalSize1), Math.abs(stickDiagonalSize2));
        double stickSize = stickDiagonalSize / Math.sqrt(1 + nowLength * nowLength);
        double stickThick = stickSize * nowLength;

        Point stickVector = Direction.fromRadian(nowAngle).getVector().norm();
        Point stickThickVector = new Point(-stickVector.getY(), stickVector.getX()).norm();


        if (noNeedRedraw || iNeedRedraw) {
            canvas.fill(new Polygon(Arrays.asList(
                    center.add(stickVector.multiple(stickSize / 2 - stickThick * 2 / 3)).add(stickThickVector.multiple(stickThick * nowThickness / 2)),
                    center.add(stickVector.multiple(stickSize / 2 - stickThick * 2 / 3)).add(stickThickVector.multiple(-stickThick * nowThickness / 2)),
                    center.add(stickVector.multiple(stickThick * 2 / 3 - stickSize / 2)).add(stickThickVector.multiple(-stickThick * nowThickness / 2)),
                    center.add(stickVector.multiple(stickThick * 2 / 3 - stickSize / 2)).add(stickThickVector.multiple(stickThick * nowThickness / 2))
            )));
        }

        IDrawable child0 = get(0).get();
        if (child0 == null && (noNeedRedraw || iNeedRedraw)) {
            canvas.fill(Arc.circle(center.add(stickVector.multiple(stickThick * 2 / 3 - stickSize / 2)), stickThick * nowThickness / 2));
        } else if (child0 != null) {
            Polygon p = new Polygon(Arrays.asList(
                    center.add(stickVector.multiple(-stickSize / 2)).add(stickThickVector.multiple(stickThick / 2)),
                    center.add(stickVector.multiple(-stickSize / 2)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(-stickSize / 2 + stickThick)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(-stickSize / 2 + stickThick)).add(stickThickVector.multiple(stickThick / 2))
            ));
            canvas.clip(p.getBoundingRectangle());

            if (noNeedRedraw) {
                child0.forceDraw(canvas);
            } else if (iNeedRedraw) {
                child0.setRedraw();
                child0.forceDraw(canvas);
            } else {
                child0.draw(canvas);
            }
            canvas.restore();
        }

        IDrawable child1 = get(1).get();
        if (child1 == null && (noNeedRedraw || iNeedRedraw)) {
            canvas.fill(Arc.circle(center.add(stickVector.multiple(stickSize / 2 - stickThick * 2 / 3)), stickThick * nowThickness / 2));
        } else if (child1 != null) {
            Polygon p = new Polygon(Arrays.asList(
                    center.add(stickVector.multiple(stickSize / 2)).add(stickThickVector.multiple(stickThick / 2)),
                    center.add(stickVector.multiple(stickSize / 2)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(stickSize / 2 - stickThick)).add(stickThickVector.multiple(-stickThick / 2)),
                    center.add(stickVector.multiple(stickSize / 2 - stickThick)).add(stickThickVector.multiple(stickThick / 2))
            ));
            canvas.clip(p.getBoundingRectangle());

            if (noNeedRedraw) {
                child1.forceDraw(canvas);
            } else if (iNeedRedraw) {
                child1.setRedraw();
                child1.forceDraw(canvas);
            } else {
                child1.draw(canvas);
            }
            canvas.restore();
        }
    }

    public Rectangle getBoundingRectangle(Rectangle bound) {
        double nowAngle = angle.get();
        double nowLength = length.setDep(bound).get();

        Point center = bound.getCenter();
        Size size = bound.getSize();

        double sizeRad = new Direction(size.asPoint()).toRadian();

        double diagonalAngle1 = nowAngle + Math.atan(nowLength);
        double diagonalAngle2 = -nowAngle + Math.atan(nowLength);
        double diagonalReferenceRad1 = new Direction(Direction.fromRadian(diagonalAngle1).getVector().abs()).toRadian();
        double diagonalReferenceRad2 = new Direction(Direction.fromRadian(diagonalAngle2).getVector().abs()).toRadian();

        double stickDiagonalSize1 = diagonalReferenceRad1 > sizeRad ? size.getWidth() / Math.cos(diagonalAngle1) :
                size.getHeight() / Math.sin(diagonalAngle1);

        double stickDiagonalSize2 = diagonalReferenceRad2 > sizeRad ? size.getWidth() / Math.cos(diagonalAngle2) :
                size.getHeight() / Math.sin(diagonalAngle2);

        double stickDiagonalSize = Math.min(Math.abs(stickDiagonalSize1), Math.abs(stickDiagonalSize2));
        double stickSize = stickDiagonalSize / Math.sqrt(1 + nowLength * nowLength);
        double stickThick = stickSize * nowLength;

        Point stickVector = Direction.fromRadian(nowAngle).getVector().norm();
        Point stickThickVector = new Point(-stickVector.getY(), stickVector.getX()).norm();

        return new Polygon(Arrays.asList(
                center.add(stickVector.multiple(stickSize / 2)).add(stickThickVector.multiple(stickThick / 2)),
                center.add(stickVector.multiple(stickSize / 2)).add(stickThickVector.multiple(-stickThick / 2)),
                center.add(stickVector.multiple(-stickSize / 2)).add(stickThickVector.multiple(stickThick / 2)),
                center.add(stickVector.multiple(-stickSize / 2)).add(stickThickVector.multiple(-stickThick / 2)))).getBoundingRectangle();
    }

    @Override
    protected boolean parentRedrawSatisfy() {
        return false;
    }
}
