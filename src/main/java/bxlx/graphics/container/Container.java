package bxlx.graphics.container;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2017.01.09..
 */
public class Container<T extends IDrawable> extends SizeChangeableContainer<T, Container<T>> {
    private final ChangeableValue<Integer> forceRedrawPrevLayer;

    public Container() {
        this(new ArrayList<>());
    }

    public Container(List<T> children) {
        this(children, 0);
    }

    @Override
    public Container<T> getThis() {
        return this;
    }

    public Container(List<T> children, int forceRedrawPrevLayer) {
        super(children);
        this.forceRedrawPrevLayer = new ChangeableValue<>(this, forceRedrawPrevLayer);
    }

    @Override
    protected boolean parentRedrawSatisfy() {
        return forceRedrawPrevLayer.get() > 0;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        Redraw redraw = needRedraw();
        boolean noNeedRedraw = redraw.noNeedRedraw();
        boolean iNeedRedraw = redraw.iNeedRedraw();

        for (int i = 0; i < size(); ++i) {
            IDrawable child = get(i).get();
            if (child == null) {
                continue;
            }

            Redraw childRedraw = child.needRedraw();
            boolean setINeedRedraw = false;
            if (!iNeedRedraw && childRedraw.needRedraw()) {
                if (i > 0 && childRedraw.parentNeedRedraw()) {
                    i = Math.max(-1, i - forceRedrawPrevLayer.get() - 1);
                    iNeedRedraw = true;
                    continue;
                }
                setINeedRedraw = true;
            }

            if (noNeedRedraw) {
                child.forceDraw(canvas);
            } else if (iNeedRedraw) {
                child.setRedraw();
                child.forceDraw(canvas);
            } else {
                child.draw(canvas);
            }

            if (setINeedRedraw) {
                iNeedRedraw = true;
            }
        }
    }
}
