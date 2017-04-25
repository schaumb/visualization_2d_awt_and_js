package bxlx.graphisoft;

import bxlx.general.IGame;
import bxlx.graphics.Color;
import bxlx.graphics.Font;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Size;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.TransformerContainer;
import bxlx.graphics.fill.Text;
import bxlx.system.ColorScheme;
import bxlx.system.FPS;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;
import bxlx.system.functional.ValueOrSupplier;

/**
 * Created by qqcs on 2017.01.18..
 */
public class Game implements IGame {
    @Override
    public Game init() {
        ColorScheme.setCurrentColorScheme(new ColorScheme(
                Color.WHITE,
                new Color(0xffc1c1c1),
                new Color(0xffc1c1c1).getScale(Color.WHITE, 0.5),
                null,
                new Color(0xffc1c1c1),
                new Color(0xff0071ff),
                new Color(0xff18445f),
                new Font("Roboto", 10, false, false),
                new Color(0xff18445f),
                Color.WHITE
        ));
        SystemSpecific.get().setMinimumSize(new Size(640, 480));
        return this;
    }

    private final Timer timer = new Timer(1000);
    private final Text text = new Text(() -> getText());
    private int counter = 0;

    @Override
    public ValueOrSupplier<IDrawable> getMain() {
        return new ValueOrSupplier<>(
                new Builder.TransformContainerBuilder<>(
                        new TransformerContainer<>(Builder.container().get(), i -> Builder.make(i).makeColored(Color.BLACK).get()))
                        .add(text)
                        .makeBackgrounded(Color.WHITE)
                        .get());
    }

    private String getText() {
        if (timer.elapsed()) {
            ++counter;
            timer.setStart();
        }
        return "Elapsed seconds: " + counter;
    }

}
