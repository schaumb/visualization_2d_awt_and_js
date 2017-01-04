package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.Text;
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
    private Container container = new Container();
    private Button r1 = new Button("");
    private Button r2 = new Button("Gomb2");
    private Text text = new Text("message");

    @Override
    public boolean render(ICanvas c) {
        c.clearCanvas(Color.WHITE);

        container.draw(c);

        fps.draw(c);
        if (timer.elapsed()) {
            timer.setStart();
        }

        if (timer2.elapsed()) {
            timer2.setStart();
        }

        return false;
    }

    public TestMain() {
        SystemSpecific.get().setDrawFunction(this);
        MouseInfo.get(); // init mouseinfo
        SystemSpecific.get().readTextFileAsync("test2.txt", this);

        container.add(r1);
        container.add(text);
        text.setColor(Color.GREEN);
        container.add(r2);
        container.setMargin(5);
        container.setSpaceBetween(6);
    }

    @Override
    public void accept(String s) {
        data = s;
    }
}
