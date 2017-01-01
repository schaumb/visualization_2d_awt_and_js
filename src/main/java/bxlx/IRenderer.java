package bxlx;

import bxlx.graphics.ICanvas;

/**
 * Created by qqcs on 2016.12.24..
 */
public interface IRenderer {
    /**
     * @param canvas is the canvas
     * @return need more render after this
     */
    boolean render(ICanvas canvas);
}
