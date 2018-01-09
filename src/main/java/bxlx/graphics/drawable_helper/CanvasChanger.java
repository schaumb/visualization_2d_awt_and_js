package bxlx.graphics.drawable_helper;

import bxlx.graphics.ICanvas;
import bxlx.system.Observable;
import bxlx.system.Observer;

import java.util.List;

public abstract class CanvasChanger extends Observable<List<Integer>> implements Observer<List<Integer>> {
    private final CanvasChanger andThen;

    CanvasChanger(CanvasChanger andThen) {
        this.andThen = andThen;
        if (andThen != null) {
            andThen.addObserver(this);
        }
    }

    @Override
    public void update(Observable<List<Integer>> observable, List<Integer> from) {
        notifyObservers(from);
    }

    public final void change(ICanvas canvas, int nTh, int max) {
        makeChange(canvas, nTh, max);

        if(andThen != null) {
            andThen.change(canvas, nTh, max);
        }
    }

    public final void restore(ICanvas canvas) {
        if(andThen != null) {
            andThen.restore(canvas);
        }

        makeRestore(canvas);
    }

    protected abstract void makeChange(ICanvas canvas, int nTh, int max);

    protected abstract void makeRestore(ICanvas canvas);

    public static final CanvasChanger NO_CHANGE = new CanvasChanger(null) {
        @Override
        protected void makeChange(ICanvas canvas, int nTh, int max) {
        }

        @Override
        protected void makeRestore(ICanvas canvas) {
        }
    };
}
