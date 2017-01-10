package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawRectangle extends ChangeableDrawable {
    private double percent;

    public DrawRectangle() {
        this(1);
    }

    public DrawRectangle(double percent) {
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    public DrawRectangle setPercent(double percent) {
        this.percent = percent;
        setRedraw();
        return this;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        canvas.fill(getRectangle(canvas.getBoundingRectangle()));
    }

    public Rectangle getRectangle(Rectangle rectangle) {
        return new Rectangle(
                rectangle.getStart().add(rectangle.getSize().asPoint().multiple((1 - percent) / 2)),
                rectangle.getEnd().add(rectangle.getSize().asPoint().multiple((percent - 1) / 2))
        );
    }
}
