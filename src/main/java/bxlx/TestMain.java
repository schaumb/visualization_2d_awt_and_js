package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.fill.Splitter;
import bxlx.system.Button;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.MyConsumer;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, IMouseEventListener, MyConsumer<String> {

    private ICanvas c;
    private Splitter splitter = new Splitter(true, -200, null,
            Builder.text("Menu").makeBackgrounded(Color.WHITE));

    private Timer timer = new Timer(3000);

    @Override
    public boolean render() {
        splitter.draw(c);
        if (timer.elapsed()) {
            timer.setStart();
        }
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        c = canvas;
        splitter.forceDraw(c);
    }

    public TestMain() {
        MouseInfo.get(); // init mouseinfo
        SystemSpecific.get().setMouseEventListenerQueue(this);
        SystemSpecific.get().readTextFileAsync("text2.txt", getAsConsumer());

        splitter.setFirst(Builder.navigator(null,
                new Button(Builder.text("TODO"), b -> {
                    splitter.getSeparate().setElem(-200 - splitter.getSeparate().get());
                    b.setOnlyForceDraw();
                }, null, null),
                Builder.imageKeepAspectRatio("kep.jpg", 1, 0), 40, Color.WHITE));
        SystemSpecific.get().setDrawFunction(this);
    }

    @Override
    public void move(Point position) {

    }

    @Override
    public void down(Point where, boolean leftButton) {
    }

    @Override
    public void up(Point where, boolean leftButton) {
    }

    @Override
    public void accept(String data) {

    }
}
