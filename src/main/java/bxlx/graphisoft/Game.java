package bxlx.graphisoft;

import bxlx.general.IGame;
import bxlx.general.MenuCreator;
import bxlx.graphics.Color;
import bxlx.graphics.Font;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Size;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.Splitter;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
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

    @Override
    public ValueOrSupplier<IDrawable> getMain() {
        return new ValueOrSupplier<>(
                new Splitter(false, r -> Math.min(Math.min(100, r.getSize().getHeight() / 2), r.getSize().getWidth() / 12),
                        new MenuCreator()
                                .setLogo("logo.png")
                                .setUrl("http://graphisoft.hu")
                                .addButton(Builder.getColorSchemeClickable(false)
                                        .makeButton(r -> SystemSpecific.get().open("task.pdf"))
                                        .with(10, 8, Builder.text("Feladat", "Kijelentkezés").get())
                                        .get())
                                .addButton(Builder.getColorSchemeClickable(false)
                                        .makeButton(r -> SystemSpecific.get().logout())
                                        .with(10, 8, Builder.text("Kijelentkezés").get())
                                        .get())
                                .setButtonsSize(-1000)
                                .getMenu()
                        , null)
        );
    }
}
