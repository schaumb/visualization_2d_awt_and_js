package bxlx.graphics;

import bxlx.graphics.drawable_helper.CanvasChanger;
import bxlx.system.Observable;
import bxlx.system.SystemSpecific;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DrawableContainer<T extends Drawable> extends Drawable {
    public static final List<Integer> ALL_CHILD = Collections.singletonList(-1);
    private List<T> child;
    private CanvasChanger canvasChanger;

    public DrawableContainer() {
        this(CanvasChanger.NO_CHANGE);
    }

    protected DrawableContainer(CanvasChanger canvasChanger) {
        this(new ArrayList<>(), canvasChanger);
    }

    protected DrawableContainer(ArrayList<T> child, CanvasChanger canvasChanger) {
        this.child = child;
        this.canvasChanger = canvasChanger;

        this.canvasChanger.addObserver((observable, from) -> setRedrawChild(from));
    }

    @Override
    public void update(Observable<Drawable> observable, Drawable from) {
        if (!SystemSpecific.get().isEquals(this, from)) {
            boolean found = false;
            for (T drawable : child) {
                if (SystemSpecific.get().isEquals(drawable, from)) {
                    found = true;
                    updateFromChild(drawable);
                    break;
                }
            }
            if (!found) {
                updateFromInside();
            }
        }
        super.update(observable, from);
    }

    protected void updateFromInside() {
        setRedrawAllChild();
    }

    protected abstract void updateFromChild(T drawable);

    protected void setRedrawAllChild() {
        setRedrawChild(ALL_CHILD);
    }

    protected void setRedrawChild(List<Integer> child) {
        if (child.size() == 1 && child.get(0) == -1) {
            for (T drawable : DrawableContainer.this.child) {
                if (drawable != null) {
                    drawable.update(this,this);
                }
            }
        } else {
            for (Integer i : child) {
                T drawable = DrawableContainer.this.get(i);

                if (drawable != null) {
                    drawable.update(this,this);
                }
            }
        }
    }

    @Override
    protected void forceDraw(ICanvas canvas) {
        int size = child.size();
        for (int i = 0; i < size; ++i) {
            T drawable = get(i);

            if(drawable != null && drawable.isNeedRedraw()) {
                canvasChanger.change(canvas, i, size);

                drawable.draw(canvas);

                canvasChanger.restore(canvas);
            }
        }
    }

    public DrawableContainer<T> add(T drawable) {
        child.add(drawable);

        if (drawable != null) {
            drawable.addObserver(this);
            drawable.setRedraw();
        }
        return this;
    }

    public T get(int index) {
        return child.get(index);
    }

    public DrawableContainer<T> set(int index, T drawable) {
        while (index >= child.size()) {
            add(null);
        }
        T prev = get(index);

        if(prev != null) {
            prev.setRedraw();
            prev.removeObserver(this);
        }

        child.set(index, drawable);

        if (drawable != null) {
            drawable.addObserver(this);
            drawable.setRedraw();
        }

        return this;
    }
}
