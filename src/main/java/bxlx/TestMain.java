package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Navigator;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawArc;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.system.Consumer;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

import java.util.Arrays;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, IMouseEventListener, Consumer<String> {

    private ICanvas c;
    private Navigator navigator = new Navigator(null, null, new Container(Arrays.asList(new ColoredDrawable(new DrawRectangle(), Color.WHITE),
            new ColoredDrawable(DrawArc.circle(true), Color.CYAN))), 50);
    private Timer timer = new Timer(3000);

    @Override
    public boolean render() {
        //c.clearCanvas(Color.WHITE);

        //c.setColor(Color.ORANGE);

        //Point center = c.getBoundingRectangle().getCenter();
        //Shape test =
        // new Arc(center, 100, 0, Math.PI / 4 * 3);
        // new Rectangle(center.add(-50), center.add(new Point(50, 100)));
        // Polygon.nGon(8, center, 100, 0);
        // Polygon.ellipseNGon(5, center, c.getBoundingRectangle().getSize().asPoint().multiple(1.0 / 2).asSize(), 0);
        // c.fill(test.getRotated(timer.percent() * Math.PI * 2));
        // c.fill(test.getScaled(timer.percent() * 2));
        // c.fill(test.getTranslated(Point.same(timer.percent() * 100)));

        navigator.draw(c);
        if (timer.elapsed()) {
            timer.setStart();
        }
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
