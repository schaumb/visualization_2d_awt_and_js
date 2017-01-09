package bxlx;

import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
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
    private Button button;

    private ICanvas lastCanvas;

    @Override
    public boolean render(ICanvas c) {
        if (lastCanvas != c) {
            button.forceDraw(c);
        } else {
            button.draw(c);
        }
        //container.forceRedraw(c);
        fps.draw(c);
        /*if (timer.elapsed()) {
            timer.setStart();
        }

        if (timer2.elapsed()) {
            timer2.setStart();
        }*/
        lastCanvas = c;
        return true;
    }

    public TestMain() {
        SystemSpecific.get().setDrawFunction(this);
        MouseInfo.get(); // init mouseinfo
        SystemSpecific.get().setMouseEventListener(this);
        button = new Button("Ello");
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
