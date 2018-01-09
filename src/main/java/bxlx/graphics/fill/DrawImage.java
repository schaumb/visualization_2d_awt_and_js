package bxlx.graphics.fill;

import bxlx.graphics.Color;
import bxlx.graphics.Drawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 2017.01.12..
 */
public class DrawImage extends Drawable {
    private String fileName;

    public DrawImage(String fileName) {
        this.fileName = fileName;
        SystemSpecific.get().preLoad(fileName, true);

        SystemSpecific.get().imageSize(fileName).addObserver((observable, from) -> setRedraw());
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

    public double getOriginalAspectRatio() {
        return SystemSpecific.get().imageSize(fileName).get().getRatio();
    }

    public Color getColor(Rectangle bound, Point position) {
        Point pos = position.add(bound.getStart().negate()).multiple(bound.getSize().asPoint().inverse());

        return SystemSpecific.get().getColor(fileName, Math.max(0, Math.min(1, pos.getX())), Math.max(0, Math.min(1, pos.getY())));
    }

    @Override
    protected void forceDraw(ICanvas canvas) {
        canvas.drawImage(fileName, canvas.getBoundingRectangle());
    }
}
