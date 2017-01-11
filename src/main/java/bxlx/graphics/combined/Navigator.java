package bxlx.graphics.combined;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.Magnifying;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Stick;
import bxlx.system.Button;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Navigator extends DrawableWrapper<Splitter> {

    private IDrawable makeButton(Button button) {
        if (button == null) {
            return null;
        }
        return Builder.make(button).makeMargin(3);
    }

    private boolean changed = false;
    private double zoom = 1;
    private double shiftX = 0;
    private double shiftY = 0;

    public Navigator(Button upLeft, Button upRight, IDrawable main, double buttonsThick) {
        super(new Splitter(0, null, null));

        getWrapped().setSecond(
                Splitter.threeWaySplit(false, -buttonsThick * 2,
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(upLeft),
                                makeButton(new Button(new DrawNGon(3, Math.PI / 2, true), null, null, null)),
                                makeButton(upRight)),
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(new Button(new DrawNGon(3, Math.PI, true), null, null, null)),
                                new WrapperClass(main),
                                makeButton(new Button(new DrawNGon(3, 0, true), null, null, null))),
                        Splitter.threeWaySplit(true, -buttonsThick * 2,
                                makeButton(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(false)), null, null, null)),
                                makeButton(new Button(new DrawNGon(3, -Math.PI / 2, true), null, null, null)),
                                makeButton(new Button(new Stick(Math.PI / 3, 0.4, 0.7, null, new Magnifying(true)), null, null, null)))
                )
        );
    }

    private class WrapperClass extends DrawableWrapper {
        public WrapperClass(IDrawable wrapped) {
            super(wrapped);
        }

        @Override
        public boolean needRedraw() {
            return changed || super.needRedraw();
        }

        @Override
        public void forceDraw(ICanvas canvas) {
            boolean forcedRedraw = !needRedraw() || changed;

            // TODO
            if (forcedRedraw) {
                super.forceDraw(canvas);
            } else {
                getWrapped().draw(canvas);
            }
        }
    }
}
