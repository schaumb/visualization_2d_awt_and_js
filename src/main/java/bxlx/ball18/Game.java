package bxlx.ball18;

import bxlx.general.IGame;
import bxlx.graphics.Color;
import bxlx.graphics.Drawable;
import bxlx.graphics.Size;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.container.Wrapper;
import bxlx.graphics.drawable_helper.ChangerBuilder;
import bxlx.graphics.drawable_helper.ObservableRectangleTranslator;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.Text;
import bxlx.system.ObservableValue;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

import java.util.Collections;

public class Game implements IGame {
    ObservableValue<Size> canvasSize;
    ObservableValue<Drawable> main;
    Timer timer = new Timer(2000);
    ObservableValue<Double> pingPong = new ObservableValue<>(() -> Math.abs(timer.get() * 2 - 1), Collections.singletonList(timer));
    boolean needRestart = false;

    @Override
    public Game init(ObservableValue<Size> canvasSize) {
        this.canvasSize = canvasSize;

        ObservableValue<String> text = new ObservableValue<>();
        this.main = new ObservableValue<>(new Container<>()
                .add(new Wrapper<>(ChangerBuilder.paint().setColor(Color.WHITE).get()).add(new DrawRectangle()))
                .add(new Wrapper<>(ChangerBuilder.clip(ObservableRectangleTranslator.aspectRatioObserve(
                        new ObservableValue<>(() -> pingPong.get() / 5 + 0.9, Collections.singletonList(pingPong)),
                        new ObservableValue<>(() -> (pingPong.get() - pingPong.get() % 0.2501) / 0.75,
                                Collections.singletonList(pingPong)), pingPong)).get())
                        .add(new Splitter(new ObservableValue<>(() -> canvasSize.get().getRatio() > 1,
                                Collections.singletonList(canvasSize)), pingPong,
                                new Wrapper<>(ChangerBuilder.paint().setColor(Color.GREEN).get()).add(new DrawRectangle()),
                                new Wrapper<>(ChangerBuilder.paint().setColor(Color.RED).get()).add(new DrawRectangle())))
                        .add(new Text(text, new ObservableValue<>(), new ObservableValue<>(0))))
                .add(new Wrapper<>(ChangerBuilder.paint().setColor(Color.BLACK).get()).add(new ClickPath())));

        SystemSpecific.get().openSocket("localhost:11223", text::setValue,
                atClose -> SystemSpecific.get().log("Closed connection because: " + atClose),
                socket -> socket.write("Connected"));

        return this;
    }

    @Override
    public ObservableValue<Drawable> getMain() {
        return main;
    }

    @Override
    public void tick() {
        if (needRestart) {
            timer.setStart();
            needRestart = false;
        }
        timer.tick();
        if (timer.percent() == 1.0) {
            needRestart = true;
        }
    }
}
