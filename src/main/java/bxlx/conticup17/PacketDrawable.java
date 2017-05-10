package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.combined.Builder;

/**
 * Created by qqcs on 5/10/17.
 */
public class PacketDrawable extends ChangeableDrawable {
    @Override
    protected void forceRedraw(ICanvas canvas) {
        Builder.background()
                .makeColored(Color.BLUE)
                .get().draw(canvas);
    }
}
