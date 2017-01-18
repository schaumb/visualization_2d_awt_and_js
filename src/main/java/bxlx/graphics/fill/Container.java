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
    public void add(IDrawable elem) {
        super.add(elem);
    }

    @Override
    public ChangeableValue<IDrawable> get(int index) {
        return super.get(index);
    }

    @Override
    protected boolean parentRedrawSatisfy() {
        return true;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        if (size() == 0) {
            return;
        }

        Redraw redraw = needRedraw();

        for (int i = 0; i < size(); ++i) {
            IDrawable child = get(i).get();
            if (child == null) {
                continue;
            }

            if (!redraw.iNeedRedraw() && child.needRedraw().parentNeedRedraw()) {
                redraw.setINeedRedraw();
                if (i > 0) {
                    i = Math.max(-1, i - forceRedrawPrevLayer.get() - 1);
                    continue;
                }
            }

            if (redraw.noNeedRedraw() || redraw.iNeedRedraw()) {
                child.forceDraw(canvas);
            } else if (get(i).isChanged()) {
                child.setOnlyForceDraw();
                child.forceDraw(canvas);
            } else {
                child.draw(canvas);
            }
        }
    }
}
