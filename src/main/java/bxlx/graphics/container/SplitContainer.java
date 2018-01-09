package bxlx.graphics.container;

import bxlx.graphics.Drawable;
import bxlx.graphics.DrawableContainer;
import bxlx.graphics.drawable_helper.ChangerBuilder;
import bxlx.graphics.drawable_helper.RectangleTranslator;

/**
 * Created by qqcs on 2017.01.04..
 */
public class SplitContainer<T extends Drawable> extends DrawableContainer<T> {

    public SplitContainer(boolean xSplit) {
        super(ChangerBuilder.clip(xSplit ? RectangleTranslator.SPLIT_X : RectangleTranslator.SPLIT_Y).get());
    }

    @Override
    protected void updateFromChild(T drawable) {
        // OK :)
    }
}
