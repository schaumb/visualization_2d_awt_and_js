package bxlx.graphics.container;

import bxlx.graphics.Drawable;
import bxlx.graphics.DrawableContainer;

/**
 * Created by qqcs on 2017.01.09..
 */
public class Container<T extends Drawable> extends DrawableContainer<T> {
    @Override
    protected void updateFromChild(T drawable) {
        setRedrawAllChild();
    }
}
