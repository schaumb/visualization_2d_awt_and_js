package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
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

        String btn = "Click this button,\nor I will kill you ";
        button.setText(btn.substring(0, (int) (timer.percent() * btn.length())));
        button.draw(c);

        c.setColor(Color.ORANGE);
        c.setFont("sans-serif", 20, false, false);
        c.fillText("Mouse at: " + MouseInfo.get().getPosition().getX() +
                " " + MouseInfo.get().getPosition().getY(), c.getBoundingRectangle().getCenter().add(-70));
        c.fillText("Left is clicked: " + MouseInfo.get().isLeftClicked(),
                c.getBoundingRectangle().getCenter().add(-30));
        if (timer.elapsed()) {
            timer.setStart();
        }
        if (data != null) {
            c.fillText(data, new Point(0, 80));
        }
        String[] args = SystemSpecific.get().getArgs();
        for (int i = 0; i < args.length; ++i) {
            c.fillText(args[i], new Point(0, 100 + i * 20));
        }

        fps.draw(c);
        if (timer.elapsed()) {
            timer.setStart();
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
