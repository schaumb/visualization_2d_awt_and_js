package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.BackgroundDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.drawable.SquareDrawable;
import bxlx.graphics.drawable.VisibleDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.DrawNumber;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.SplitContainer;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Stick;
import bxlx.graphics.fill.Text;
import bxlx.system.Button;
import bxlx.system.Consumer;
import bxlx.system.FPS;
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
    private FPS fps = new FPS();
    private Splitter container = new Splitter(true, -200, null, null);
    private DrawNumber counterNumber = new DrawNumber(0, "/1000", "1000/1000");
    private final Color bgColor = new Color(240, 240, 240);
    private DrawNGon ng = new DrawNGon(false, true, 4, 0);
    private Timer timer = new Timer(7000);
    private Timer timer2 = new Timer(10000);
    private Timer timer3 = new Timer(11000);


    private ICanvas c;

    private DrawNGon nGon = new DrawNGon(true, false, 3, 0);
    private Stick stick = new Stick(0, 0.3, 0.33, null, nGon);
    private ColoredDrawable coloredStick = new ColoredDrawable(stick, Color.GREEN);
    private DrawRectangle rt = new DrawRectangle(0.5);

    @Override
    public boolean render() {
        c.clearCanvas(Color.WHITE);
        if (timer != null) {
            //stick.setAngle(timer.percent() * 2 * Math.PI);
            stick.setThickness(timer.percent());
            //nGon.setStartAngle(timer.percent() * 2 * Math.PI);
        }
        stick.setLength(timer3.percent());
        stick.setAngle(timer2.percent() * 2 * Math.PI);
        nGon.setStartAngle(timer2.percent() * 2 * Math.PI);
        coloredStick.draw(c);
        /*
        if (timer != null) {
            double angle = timer.percent() * 2 * Math.PI;
            if (angle > ng.getStartAngle()) {
                ng.setStartAngle(angle);
            }
        }
        container.draw(c);        if(angle < -Math.PI) {
            halfStick = halfStick.negate();
        }


        fps.draw(c);
        if (timer != null && timer.elapsed()) {
            timer.setLength(2000);
            timer.setStart();
            ng.setStartAngle(0);
        }
        */
        if (timer != null && timer.elapsed()) {
            timer.setStart();
        }
        if (timer2.elapsed()) {
            timer2.setStart();
        }
        if (timer3.elapsed()) {
            timer3.setStart();
        }
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        c = canvas;
        coloredStick.forceDraw(c);
        /*
        canvas.clearCanvas(bgColor);

        container.forceDraw(c);
        fps.forceDraw(c);*/
    }

    public TestMain() {
        MouseInfo.get(); // init mouseinfo
        SystemSpecific.get().setMouseEventListenerQueue(this);
        SystemSpecific.get().readTextFileAsync("text2.txt", this);

        VisibleDrawable visibled = new VisibleDrawable(new ColoredDrawable(new Text("Szia"), Color.ORANGE), true);
        container.setFirst(
                Splitter.threeWaySplit(false, -140,
                        new Container(Arrays.asList(new Rect(), visibled)),
                        new Container(Arrays.asList(
                                new ColoredDrawable(new DrawRectangle(), Color.WHITE),
                                new ColoredDrawable(ng, Color.CYAN)
                                //, new ColoredDrawable(new Text(SystemSpecific.get().getArgs()[0]), Color.CYAN)
                        )),
                        new ColoredDrawable(new DrawNGon(false, false, 5, 0), Color.BLUE)))
                .setSecond(new SplitContainer(Arrays.asList(
                        new SplitContainer(true, Arrays.asList(
                                new Button(new Text("-", "+"), null, () -> counterNumber.setNumber(Math.max(0, counterNumber.getNumber() - 1)), null),
                                new BackgroundDrawable(new ColoredDrawable(counterNumber, Color.ORANGE), bgColor),
                                new Button("+", null, () -> counterNumber.setNumber(Math.max(0, counterNumber.getNumber() + 1)), null)
                        )),
                        new Text("ASD"),
                        new Button(new DrawNGon(3, 0), () -> timer = new Timer(2000, ng.getStartAngle() / 2.0 / Math.PI), null, () -> counterNumber.getNumber() > 10),
                        new Button(new SquareDrawable(new MarginDrawable(new DrawRectangle(), 0.2)), () -> visibled.setVisible(true), () -> visibled.setVisible(false), () -> counterNumber.getNumber() > 10),
                        new Button(
                                new SquareDrawable(
                                        new MarginDrawable(
                                                new SplitContainer(true, Arrays.asList(
                                                        new DrawRectangle(),
                                                        null,
                                                        new DrawRectangle()
                                                ))
                                                , 0.2)
                                )
                                , () -> timer = null, null, null),
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
        if (leftButton) {
            timer = null;
        } else {
            timer = new Timer(10000, stick.getThickness());
        }
    }

    @Override
    public void accept(String data) {

    }
}
