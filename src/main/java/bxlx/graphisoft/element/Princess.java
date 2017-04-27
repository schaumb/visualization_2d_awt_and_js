package bxlx.graphisoft.element;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphisoft.Parameters;

import java.util.function.Supplier;

/**
 * Created by ecosim on 4/27/17.
 */
public class Princess extends ChangeableDrawable {
    private final DrawImage moveImage;
    private final DrawImage displayButton;
    private Point position;

    private boolean moving = false;
    private Supplier<Double> time;
    private Point toPosition;

    public Princess(int index) {
        this.moveImage = new DrawImage(Parameters.imgDir() + "q" + index + ".png");
        this.displayButton = new DrawImage(Parameters.imgDir() + "b" + index + ".png");
    }

    public DrawImage getDisplayButton() {
        return displayButton;
    }

    public void setPosition(Point point) {
        this.position = point;
    }

    public void setMove(Point destination) {
        moving = true;
        toPosition = destination;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        moveImage.forceDraw(canvas);
    }

    public void setNoMove() {
        moving = false;
        toPosition = null;
    }

    public Point getToPosition() {
        return toPosition;
    }

    public Point getPosition() {
        return position;
    }
}
