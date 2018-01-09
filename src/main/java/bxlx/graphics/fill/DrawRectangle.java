package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawRectangle extends ChangeableDrawable {
    private final ChangeableDrawable.ChangeableValue<Double> percent;

    public DrawRectangle() {
        this(1);
    }

    public DrawRectangle(double percent) {
        this.percent = new ChangeableDrawable.ChangeableValue<>(this, percent);
    }


    @Override
    public void forceRedraw(ICanvas canvas) {
        canvas.fill(getRectangle(canvas.getBoundingRectangle()));
    }

    public Rectangle getRectangle(Rectangle rectangle) {
        double nowPercent = percent.get();
        return new Rectangle(
                rectangle.getStart().add(rectangle.getSize().asPoint().multiple((1 - nowPercent) / 2)),
                rectangle.getEnd().add(rectangle.getSize().asPoint().multiple((nowPercent - 1) / 2))
        );
    }
}
