package bxlx.graphics.container;

import bxlx.graphics.Drawable;
import bxlx.graphics.DrawableContainer;
import bxlx.graphics.drawable_helper.CanvasChanger;

public class Wrapper<T extends Drawable> extends DrawableContainer<T> {
    public Wrapper(CanvasChanger canvasChanger) {
        super(canvasChanger);
    }

    @Override
    protected void updateFromChild(T drawable) {

    }
}
