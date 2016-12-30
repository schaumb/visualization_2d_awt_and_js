package bxlx.pipe;

import bxlx.IDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2016.12.24..
 */
public class Rect implements IDrawable {
    private final static double RATE = 1.0 / 3.0;

    @Override
    public void draw(ICanvas canvas) {
        Rectangle bounds = canvas.getBoundingRectangle();

        if(bounds.getSize().getWidth() <= 0 || bounds.getSize().getHeight() <= 0) {
            return;
        }

        canvas.setColor(Color.BLUE);

        canvas.fillRectangle(bounds);

        double rectSizeX = Math.min(bounds.getSize().getHeight(), bounds.getSize().getWidth()) * RATE;
        Size rectSize = Point.same(rectSizeX).asSize();
        canvas.clip(new Rectangle(bounds.getStart(), rectSize));
        canvas.clearCanvas(Color.WHITE);
        canvas.setColor(Color.BLUE);
        canvas.fillArc(Arc.circle(rectSize.asPoint().add(bounds.getStart()), rectSizeX));
        canvas.restore();

        canvas.clip(new Rectangle(new Point(bounds.getStart().getX(), bounds.getStart().getY() + bounds.getSize().getHeight() - rectSizeX), rectSize));
        canvas.clearCanvas(Color.WHITE);
        canvas.setColor(Color.BLUE);
        canvas.fillArc(Arc.circle(canvas.getBoundingRectangle().getStart().add(new Point(rectSizeX, 0)), rectSizeX));
        canvas.restore();

        canvas.clip(new Rectangle(bounds.getStart().add(bounds.getSize().asPoint())
                .add(-rectSizeX), rectSize));
        canvas.clearCanvas(Color.WHITE);
        canvas.setColor(Color.BLUE);
        canvas.fillArc(Arc.circle(canvas.getBoundingRectangle().getStart(), rectSizeX));
        canvas.restore();

        canvas.clip(new Rectangle(new Point(bounds.getStart().getX() + bounds.getSize().getWidth() - rectSizeX, bounds.getStart().getY()), rectSize));
        canvas.clearCanvas(Color.WHITE);
        canvas.setColor(Color.BLUE);
        canvas.fillArc(Arc.circle(canvas.getBoundingRectangle().getStart().add(new Point(0, rectSizeX)), rectSizeX));
        canvas.restore();
    }
}
