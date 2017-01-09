package bxlx.graphics.fill;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.DrawableContainer;

import java.util.List;

/**
 * Created by qqcs on 2017.01.09..
 */
public class Container extends DrawableContainer {
    private final int forceRedrawPrevLayer;

    public Container(List<IDrawable> children) {
        this(children, 1);
    }

    public Container(List<IDrawable> children, int forceRedrawPrevLayer) {
        super(children);
        this.forceRedrawPrevLayer = forceRedrawPrevLayer;
    }

    public Container add(IDrawable drawable) {
        children.add(drawable);
        setRedraw();
        return this;
    }

    public Container remove(IDrawable drawable) {
        children.remove(drawable);
        setRedraw();
        return this;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        if (children.isEmpty()) {
            return;
        }

        boolean forcedRedraw = !needRedraw();

        for (int i = 0; i < children.size(); ++i) {
            if(!iChanged() && children.get(i).needRedraw()) {
                setRedraw();
                if(i > 0) {
                    i = Math.max(-1, i - forceRedrawPrevLayer - 1);
                    continue;
                }
            }

            if (forcedRedraw || iChanged()) {
                children.get(i).forceDraw(canvas);
            } else {
                children.get(i).draw(canvas);
            }
        }
    }
}
