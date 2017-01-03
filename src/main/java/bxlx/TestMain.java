package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.pipe.Text;
import bxlx.system.Button;
import bxlx.system.Consumer;
import bxlx.system.FPS;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, Consumer<String> {
    private Timer timer = new Timer(6000);
    private Timer timer2 = new Timer(3000);
    private FPS fps = new FPS();
    private String data = null;

    private Button button = new Button(new Rectangle(60, 60, 200, 200), "Click this button") {
        @Override
        public void onClicked() {
            if (data != null) {
                data += " clicked";
            }
        }
    };

    @Override
    public boolean render(ICanvas c) {
        c.clearCanvas(Color.WHITE);

        Arc arc = new Arc(c.getBoundingRectangle().getCenter(),
                c.getBoundingRectangle().getSize().getShorterDimension() / 2,
                timer.percent() * 2 * Math.PI, timer2.percent() * 2 * Math.PI);

        c.setColor(Color.CYAN);
        c.fill(arc.getBoundingRectangle());
        c.setColor(Color.GREEN);
        c.fill(arc);


        Polygon p = Polygon.ellipseNGon(3, c.getBoundingRectangle().getCenter(),
                c.getBoundingRectangle().getSize(), 0);
        c.setColor(Color.ORANGE);
        c.fill(p);
        c.setColor(Color.BLACK);
        new Text("Contains: " + p.isContains(MouseInfo.get().getPosition())).draw(c);

        fps.draw(c);
        if (timer.elapsed()) {
            timer.setStart();
            timer2.setStart();
        }

        return true;
    }

    public TestMain() {
        SystemSpecific.get().setDrawFunction(this);
        SystemSpecific.get().setMouseEventListener(MouseInfo.get());
        SystemSpecific.get().readTextFileAsync("test2.txt", this);
        SystemSpecific.get().setMouseEventListener(button);
    }

    @Override
    public void accept(String s) {
        data = s;
    }
}
