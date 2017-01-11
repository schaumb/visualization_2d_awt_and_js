package bxlx;

import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Navigator;
import bxlx.graphics.fill.DrawArc;
import bxlx.system.Consumer;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, IMouseEventListener, Consumer<String> {

    private ICanvas c;
    private Navigator navigator = new Navigator(null, null, DrawArc.circle(true), 50);

    @Override
    public boolean render() {
        navigator.draw(c);
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        c = canvas;
        navigator.forceDraw(c);
    }

    public TestMain() {
        MouseInfo.get(); // init mouseinfo
        SystemSpecific.get().setMouseEventListenerQueue(this);
        SystemSpecific.get().readTextFileAsync("text2.txt", this);

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
