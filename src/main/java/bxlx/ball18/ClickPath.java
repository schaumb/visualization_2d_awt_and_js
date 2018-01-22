package bxlx.ball18;

import bxlx.graphics.Drawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Line;
import bxlx.system.MouseInfo;
import bxlx.system.ObservableValue;

public class ClickPath extends Drawable {
    private final MouseInfo mouseInfo = MouseInfo.get();

    private ObservableValue<Point> from = new ObservableValue<>();
    private ObservableValue<Point> to = new ObservableValue<>();

    public ClickPath() {
        this.from.addObserver((observable, from) -> this.setRedraw());
        this.to.addObserver((observable, from) -> this.setRedraw());

        mouseInfo.isLeftClicked().addObserver((elem, val) -> {
            final ObservableValue<Point> position = mouseInfo.getPosition();
            if (val) {
                final Point transformed = transform(position.get());
                from.setValue(transformed);
                to.setValue(transformed);
            }
            this.setRedraw();
        });

        mouseInfo.getPosition().addObserver((observable, from) -> {
            if (mouseInfo.isLeftClicked().get()) {
                to.setValue(transform(from));
            }
        });
    }

    @Override
    protected void forceDraw(ICanvas canvas) {
        if (mouseInfo.isLeftClicked().get() &&
                from.get() != null && to.get() != null) {
            canvas.drawLine(new Line(from.get(), to.get()));
        }
    }


    private Point transform(Point point) {
        return new Point(point.getX() + 25 - (point.getX() + 25) % 50.0, point.getY() + 25 - (point.getY() + 25) % 50.0);
    }
}
