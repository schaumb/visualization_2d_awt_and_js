package bxlx.graphisoft;

import bxlx.general.IGame;
import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.Font;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;
import bxlx.system.input.Button;
import bxlx.system.input.ButtonWithData;
import bxlx.system.input.RadioButtons;
import bxlx.system.input.Slider;
import bxlx.system.input.clickable.ColorSchemeClickable;

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
                new Color(0xffeeeeee),
                new Color(0xffc1c1c1),
                new Color(0xff0071ff),
                new Color(0xff18445f),
                new Font("Roboto", 10, false, false),
                new Color(0xff18445f),
                Color.WHITE
        ));

        String from = SystemSpecific.get().getArgs()[0];
        String file = SystemSpecific.get().getArgs()[1];
        SystemSpecific.get().readTextFileAsync(from, file, r -> start(r));
        return this;
    }

    public void start(String reachableFiles) {
        String[] fileNames = reachableFiles.trim().split("\n");

        int i = 0;

        int maxButton = radioButtons.size();
        int maxFiles = fileNames.length;

        for (; i < Math.min(maxButton, maxFiles); ++i) {
            radioButtons.getButton(i).setElem(
                    new ButtonWithData<>(new ColorSchemeClickable(true, true), null, null, null, fileNames[i]));
        }
        for(; i < maxButton; ++i) {
            radioButtons.getButton(i).setElem(null);
        }

        for(; i < maxFiles; ++i) {
            radioButtons.addButton(new ButtonWithData<>(new ColorSchemeClickable(true, true), null, null, null, fileNames[i]));
        }
    }

    RadioButtons<?, ?, ColorSchemeClickable, ButtonWithData<ColorSchemeClickable, String>> radioButtons =
            new RadioButtons<>(new SplitContainer<>(), i -> Builder.container().add(i).add(Builder.text(i.getData(), "10", 0)
                    .makeMargin(5).get()).makeColored(Color.BLACK).makeMargin(4).makeSquare(0, 0).get());

    ValueOrSupplier<Boolean> disabled = new ValueOrSupplier<>(true);
    ValueOrSupplier<Double> size = new ValueOrSupplier<>(0.0);

    Slider slider = new Slider(new Button<>(new ColorSchemeClickable(true, false), null, null, disabled.getAsSupplier()), false, 0);

    ClippedDrawable<RadioButtons<?, ?, ColorSchemeClickable, ButtonWithData<ColorSchemeClickable, String>>> clippedDrawable =
            new ClippedDrawable<>(radioButtons, true, r -> {
        double zz = size.get() - r.getSize().getHeight();
        disabled.setElem(zz > 0);
        return r.withStart(r.getStart()
                .add(new Point(0, Math.min(0, zz * slider.getNow().get()))));
    });
    ChangeableDrawable.ChangeableValue unused =
        new ChangeableDrawable.ChangeableValue<>(clippedDrawable, size.getAsSupplier());

    Splitter menu = Builder.splitter(true, disabled.transform(b -> b ? 0.0 : -30.0).getAsSupplier(),
            Builder.make(clippedDrawable)
                    .makeAspect(false,1, -1, i -> (double) i.getChild().get().size())
                    .makeClipped(true, c -> c.withSize(new Size(c.getSize().getWidth(), 9000000)))
                    .makeClipped(true, r -> new Rectangle(r.getStart(),
                            new Size(r.getSize().getWidth(), size.setElem(r.getSize().getHeight()).get()))).get(),
            slider)
            .get();

    Splitter main = new Splitter(true, disabled.transform(b -> b ? -50.0 : -80.0).getAsSupplier(), null, menu);


    @Override
    public ValueOrSupplier<IDrawable> getMain() {
        return new ValueOrSupplier<>(Builder.make(main).makeBackgrounded(Color.WHITE).get());
    }
}
