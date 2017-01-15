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
import bxlx.system.ValueOrSupplier;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, IMouseEventListener, MyConsumer<String> {

    private ICanvas c;
    private Splitter splitter = new Splitter(true, -200, null,
            Builder.text("Menu").makeColored(Color.BLUE).makeBackgrounded(Color.WHITE).getChild());

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

        ValueOrSupplier<Boolean> visibleNavi = new ValueOrSupplier<>(true);

        Button visibleButton = new Button(Builder.text("HIDE", "MENU").getChild(), b -> {
            visibleNavi.setElem(!visibleNavi.get());
            splitter.getFirst().get().setOnlyForceDraw();
            splitter.setRedraw();
        }, null, null);

        Button menuButton = new Button(Builder.text("MENU").getChild(), b -> {
            splitter.getSeparate().setElem(-200 - splitter.getSeparate().get());
            splitter.getFirst().get().setOnlyForceDraw();
        }, null, null);

        splitter.getFirst().setElem(Builder.navigator(visibleButton, menuButton,
                Builder.imageKeepAspectRatio("kep.jpg", 1, 0).getChild(), visibleNavi.getAsSupplier(), 40, Color.WHITE).getChild());
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
