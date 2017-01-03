package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.pipe.Rect;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, IMouseEventListener, Consumer<String> {
    private Timer timer = new Timer(6000);
    private FPS fps = new FPS();
    private Point mousePos = Point.ORIGO;
    private boolean leftIsClicked = false;
    private String data = null;

    @Override
    public boolean render(ICanvas c) {

        new Rect().draw(c);


        c.setColor(Color.ORANGE);
        c.setFont("sans-serif", 20, false, false);
        c.fillText("Mouse at: " + mousePos.getX() + " " + mousePos.getY(), c.getBoundingRectangle().getCenter().add(-70));
        c.fillText("Left is clicked: " + leftIsClicked, c.getBoundingRectangle().getCenter().add(-30));
        if (timer.elapsed()) {
            timer.setStart();
        }
        if (data != null) {
            c.fillText(data, new Point(0, 80));
        }

        fps.draw(c);

        return true;
    }


    public TestMain() {
        SystemSpecific.get().setDrawFunction(this);
        SystemSpecific.get().setMouseEventListener(this);
        SystemSpecific.get().readTextFileAsync("test2.txt", this);
    }


    @Override
    public void move(Point position) {
        mousePos = position;
    }

    @Override
    public void down(Point where, boolean leftButton) {
        mousePos = where;
        leftIsClicked |= leftButton;
    }

    @Override
    public void up(Point where, boolean leftButton) {
        mousePos = where;
        leftIsClicked &= !leftButton;
    }

    @Override
    public void accept(String s) {
        data = s;
    }
}
