package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 2017.01.12..
 */
public class DrawImage extends ChangeableDrawable {
    private String fileName;
    private final ChangeableDrawable.ChangeableValue<Double> aspectRatio;

    public DrawImage(String fileName) {
        this.fileName = fileName;
        SystemSpecific.get().preLoad(fileName, true);
        aspectRatio = new ChangeableDrawable.ChangeableValue<>(this, () -> getOriginalAspectRatio());
    }

    public String getFileName() {
        return fileName;
    }

    public DrawImage setFileName(String fileName) {
        this.fileName = fileName;
        SystemSpecific.get().preLoad(fileName, true);
        setRedraw();
        return this;
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().setIf(aspectRatio.isChanged(), IDrawable.Redraw.PARENT_NEED_REDRAW);
    }

    public double getOriginalAspectRatio() {
        return SystemSpecific.get().preLoad(fileName, true).getRatio();
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        canvas.drawImage(fileName, canvas.getBoundingRectangle());
    }

    public Color getColor(Rectangle bound, Point position) {
        Point pos = position.add(bound.getStart().negate()).multiple(bound.getSize().asPoint().inverse());

        return SystemSpecific.get().getColor(fileName, Math.max(0, Math.min(1, pos.getX())), Math.max(0, Math.min(1, pos.getY())));
    }
}
