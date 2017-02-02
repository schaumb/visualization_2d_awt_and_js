package bxlx.graphics.fill;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.DrawableContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2017.01.09..
 */
public class Container extends DrawableContainer<IDrawable> {
    private final ChangeableValue<Integer> forceRedrawPrevLayer;

    public Container() {
        this(new ArrayList<>());
    }

    public Container(List<IDrawable> children) {
        this(children, 0);
    }

    public Container(List<IDrawable> children, int forceRedrawPrevLayer) {
        super(children);
        this.forceRedrawPrevLayer = new ChangeableValue<>(this, forceRedrawPrevLayer);
    }

    @Override
    public Container add(IDrawable elem) {
        super.add(elem);
        return this;
    }

    @Override
    public ChangeableValue<IDrawable> get(int index) {
        return super.get(index);
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
            if (!iNeedRedraw && childRedraw.needRedraw()) {
                iNeedRedraw = true;
                if (i > 0 && childRedraw.parentNeedRedraw()) {
                    i = Math.max(-1, i - forceRedrawPrevLayer.get() - 1);
                    continue;
                }
            }

            if (noNeedRedraw) {
                child.forceDraw(canvas);
            } else if (iNeedRedraw) {
                child.setRedraw();
                child.forceDraw(canvas);
            } else {
                child.draw(canvas);
            }
        }
    }
}
