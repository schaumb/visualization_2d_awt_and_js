package bxlx.graphics;

import bxlx.system.Observable;
import bxlx.system.Observer;

/**
 * Created by qqcs on 2016.12.24..
 */
public abstract class Drawable extends Observable<Drawable> implements Observer<Drawable> {
    private boolean needRedraw;

    public boolean draw(ICanvas canvas) {
        if (needRedraw) {
            forceDraw(canvas);

            needRedraw = false;
            return true;
        }
        return false;
    }

    public void setRedraw() {
        setRedraw(this);
    }

    public void setRedraw(Drawable from) {
        if (!needRedraw) {
            needRedraw = true;

            notifyObservers(from);
        }
    }

    @Override
    public void update(Observable<Drawable> observable, Drawable from) {
        setRedraw(from);
    }

    public boolean isNeedRedraw() {
        return needRedraw;
    }

    protected abstract void forceDraw(ICanvas canvas);
}
