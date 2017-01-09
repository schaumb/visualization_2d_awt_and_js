package bxlx;

import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Container;
import bxlx.system.Button;
import bxlx.system.FPS;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, IMouseEventListener {
    private FPS fps = new FPS();
    private Button btn;
    private Container container = new Container();
    private Container xContainer = new Container().setxSplit(true);

    private ICanvas c;

    @Override
    public boolean render() {
        container.draw(c);
        fps.draw(c);
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        c = canvas;

        container.forceDraw(c);
        fps.forceDraw(c);
    }

    public TestMain() {
        MouseInfo.get(); // init mouseinfo
        SystemSpecific.get().setMouseEventListenerQueue(this);
        btn = new Button("Ello");
        xContainer.add(btn).add(btn);
        container.add(xContainer).add(new MarginDrawable(btn, 3, 10));

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
        if (!leftButton) {
            fps.reset();
        }
    }
}
