package bxlx.graphisoft;

import bxlx.general.IGame;
import bxlx.graphics.Color;
import bxlx.graphics.Font;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.fill.SplitContainer;
import bxlx.graphics.fill.Text;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;
import bxlx.system.input.Button;
import bxlx.system.input.Selector;

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
        return this;
    }

    @Override
    public ValueOrSupplier<IDrawable> getMain() {
        return new ValueOrSupplier<>(new SplitContainer<>()
                .add(new Selector(true, false).addText(new Selector.RectClickable(), new Text("Color test")))
                .add(Builder.text("Test Text").get())
                .add(new Button<>(new Selector.RectClickable(), r ->
                        SystemSpecific.get().open("asd.pdf"), null, () -> false)));
    }
}
