package bxlx.graphics.drawable_helper;

import bxlx.graphics.Color;
import bxlx.graphics.DrawableContainer;
import bxlx.graphics.ICanvas;
import bxlx.system.ObservableValue;

public class Painter extends CanvasChanger {
    private final ObservableValue<Color> color;
    private Color tmp;

    Painter(ObservableValue<Color> color, CanvasChanger andThen) {
        super(andThen);
        this.color = color;

        color.addObserver((observable, from) -> Painter.this.notifyObservers(DrawableContainer.ALL_CHILD));
    }

    @Override
    protected void makeChange(ICanvas canvas, int nTh, int max) {
        tmp = canvas.getColor();
        canvas.setColor(color.get());
    }

    @Override
    protected void makeRestore(ICanvas canvas) {
        canvas.setColor(tmp);
    }
}
