package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.BackgroundDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.SquareDrawable;
import bxlx.graphics.fill.DrawNumber;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.SplitContainer;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Text;
import bxlx.system.Button;
import bxlx.system.FPS;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;

import java.util.Arrays;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer, IMouseEventListener {
    private FPS fps = new FPS();
    private Splitter container = new Splitter(true, -200, null, null);
    private DrawNumber counterNumber = new DrawNumber();
    private final Color bgColor = new Color(240, 240, 240);

    private ICanvas c;

    @Override
    public boolean render() {
        container.draw(c);
        fps.draw(c);
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        canvas.clearCanvas(bgColor);
        c = canvas;

        container.forceDraw(c);
        fps.forceDraw(c);
    }

    public TestMain() {
        MouseInfo.get(); // init mouseinfo
        SystemSpecific.get().setMouseEventListenerQueue(this);


        container.setFirst(new SquareDrawable(new ColoredDrawable(new Rect(), Color.CYAN)))
                .setSecond(new SplitContainer(Arrays.asList(
                        new SplitContainer(true, Arrays.asList(
                                new Button("-", null, () -> counterNumber.setNumber(Math.max(0, counterNumber.getNumber() - 1))),
                                new BackgroundDrawable(new ColoredDrawable(counterNumber, Color.ORANGE), bgColor),
                                new Button("+", null, () -> counterNumber.setNumber(Math.max(0, counterNumber.getNumber() + 1)))
                        )),
                        new Text("ASD"),
                        new Button("FILLER", null, null),
                        new Text("ASDASD")
                )));

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
