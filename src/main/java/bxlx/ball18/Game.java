package bxlx.ball18;

import bxlx.general.IGame;
import bxlx.graphics.Color;
import bxlx.graphics.Drawable;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.container.Wrapper;
import bxlx.graphics.drawable_helper.ChangerBuilder;
import bxlx.graphics.drawable_helper.RectangleTranslator;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.system.ObservableValue;

public class Game implements IGame {
    ObservableValue<Drawable> main;

    @Override
    public Game init() {

        main = new ObservableValue<>(new Wrapper<>(ChangerBuilder
                .clip(RectangleTranslator.aspectRatio(1.0, 0.5, 0.5)).get())
                .add(new Splitter(true, 0.5,
                        new Wrapper<>(ChangerBuilder.paint().setColor(Color.GREEN).get()).add(new DrawRectangle()),
                        new Wrapper<>(ChangerBuilder.paint().setColor(Color.RED).get()).add(new DrawRectangle())
        )));

        return this;
    }

    @Override
    public ObservableValue<Drawable> getMain() {
        return main;
    }
}
