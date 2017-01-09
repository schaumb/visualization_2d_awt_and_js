package bxlx.system;

import bxlx.graphics.ICanvas;

/**
 * Created by qqcs on 2016.12.24..
 */
public interface IRenderer {
    /**
     * @return need more render after this
     */
    boolean render();

    void setCanvas(ICanvas canvas);
}
