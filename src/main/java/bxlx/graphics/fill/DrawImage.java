package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 2017.01.12..
 */
public class DrawImage extends ChangeableDrawable {
    private String fileName;

    public DrawImage(String fileName) {
        this.fileName = fileName;
        SystemSpecific.get().preLoad(fileName, true);

    }

    public String getFileName() {
        return fileName;
    }

    public DrawImage setFileName(String fileName) {
        this.fileName = fileName;
        setRedraw();
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        canvas.drawImage(fileName, canvas.getBoundingRectangle());
    }
}
