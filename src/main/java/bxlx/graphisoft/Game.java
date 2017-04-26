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
import bxlx.graphics.fill.Text;
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
    ValueOrSupplier<Boolean> disabled = new ValueOrSupplier<>(true);
    ValueOrSupplier<Double> size = new ValueOrSupplier<>(0.0);

    RadioButtons<?, ?, ColorSchemeClickable, ButtonWithData<ColorSchemeClickable, String>> radioButtons;

    ValueOrSupplier<String> longestText;

    Slider slider;

    ClippedDrawable<RadioButtons<?, ?, ColorSchemeClickable, ButtonWithData<ColorSchemeClickable, String>>> clippedDrawable;

    Splitter menu;

    GameViewer gameViewer;

    Splitter main;

    public Game() {
        longestText = new ValueOrSupplier<>(() -> {
            String longest = "i";
            int longestVal = 0;
            for(int i = 0; i < radioButtons.size(); ++i) {
                String str = radioButtons.getButton(i).get().getData();
                int lengthOfStr = SystemSpecific.get().stringLength(ColorScheme.getCurrentColorScheme().font, str);
                if (lengthOfStr > longestVal) {
                    longest = str;
                    longestVal = lengthOfStr;
                }
            }
            return longest;
        });
        radioButtons = new RadioButtons<>(new SplitContainer<>(), i -> Builder.container().add(i).add(Builder.text(i.getData(), longestText.getAsSupplier(), 0)
                .makeColored(ColorScheme.getCurrentColorScheme().textColor)
                .makeMargin(5).get()).makeColored(Color.BLACK).makeMargin(4).makeSquare(0, 0).get());
        slider = new Slider(new Button<>(new ColorSchemeClickable(true, false), null, null, disabled.getAsSupplier()), false, 0);
        clippedDrawable = new ClippedDrawable<>(radioButtons, true, r -> {
            double zz = size.get() - r.getSize().getHeight();
            disabled.setElem(zz > 0);
            return r.withStart(r.getStart()
                    .add(new Point(0, Math.min(0, zz * slider.getNow().get()))));
        });
        menu = Builder.splitter(true, disabled.transform(b -> b ? 0.0 : -30.0).getAsSupplier(),
                Builder.make(clippedDrawable)
                        .makeAspect(false,1, -1, i -> (double) i.getChild().get().size())
                        .makeClipped(true, c -> c.withSize(new Size(c.getSize().getWidth(), 9000000)))
                        .makeClipped(true, r -> new Rectangle(r.getStart(),
                                new Size(r.getSize().getWidth(), size.setElem(r.getSize().getHeight()).get()))).get(),
                slider)
                .get();
        gameViewer = new GameViewer(() -> {
            Integer res = radioButtons.getSelected().get();
            if(res == null || res == -1) {
                return null;
            }
            return radioButtons.getButton(res).get().getData();
        });
        main = new Splitter(true, disabled.transform(b -> b ? -50.0 : -80.0).getAsSupplier(), gameViewer, menu);
    }


    @Override
    public Game init() {
        if(SystemSpecific.get().getArgs().length < 2) {
            SystemSpecific.get().setArgs(new String[] {
                    "/game/inc/process/p.gameLoad.php", // "."
                    "process=%d&level=%s" // "%1$s.txt"
            });
        }
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

        new ChangeableDrawable.ChangeableValue<>(clippedDrawable, size.getAsSupplier());
        readReachableFiles();
        return this;
    }

    public void readReachableFiles() {
        if(SystemSpecific.get().getArgs().length < 2) {
            SystemSpecific.get().log("No file argument");
            return;
        }
        String from = SystemSpecific.get().getArgs()[0];
        String file = String.format(SystemSpecific.get().getArgs()[1], 1, "0");
        SystemSpecific.get().readTextFileAsync(from, file, r -> reachable(file, r));
    }

    public void reachable(String fileName, String reachableFiles) {
        if(reachableFiles == null) {
            SystemSpecific.get().log("Can not reach file: " + fileName + ", try again after 2 sec");
            SystemSpecific.get().runAfter(() -> readReachableFiles(), 2000);
            return;
        }

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

    @Override
    public ValueOrSupplier<IDrawable> getMain() {
        return new ValueOrSupplier<>(Builder.make(main).makeBackgrounded(Color.WHITE).get());
    }
}
