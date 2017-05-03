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
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;
import bxlx.system.input.Button;
import bxlx.system.input.ButtonWithData;
import bxlx.system.input.RadioButtons;
import bxlx.system.input.Slider;
import bxlx.system.input.clickable.CheckboxClickable;
import bxlx.system.input.clickable.ColorSchemeClickable;

/**
 * Created by qqcs on 2017.01.18..
 */
public class Game implements IGame {
    private ChangeableDrawable.ChangeableValue<String> longestText;
    private ChangeableDrawable.ChangeableValue<Double> size;
    private ChangeableDrawable.ChangeableValue<Boolean> settingIsOn;

    private RadioButtons<?, ?, ColorSchemeClickable, ButtonWithData<ColorSchemeClickable, String>> radioButtons;

    private ChangeableDrawable main;

    public Game() {

        radioButtons = new RadioButtons<>(new SplitContainer<>(), i -> Builder.container()
                .add(i).add(Builder.text(() -> i.getData(), longestText.getAsSupplier(), 0)
                .makeColored(ColorScheme.getCurrentColorScheme().textColor)
                .makeMargin(5).get()).makeColored(Color.BLACK).makeMargin(4).makeSquare(0, 0).get());

        longestText = new ChangeableDrawable.ChangeableValue<>(radioButtons, () -> {
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

        Button<ColorSchemeClickable> butt = new Button<>(new ColorSchemeClickable(true, false), null, null, () -> true);

        Slider slider = new Slider(butt, false, 0);

        IDrawable menu = Builder.splitter(true, () -> butt.getDisabled().get() ? 0.0 : -30.0,
                Builder.splitter(false, -50, Builder.make(new ClippedDrawable<>(radioButtons, true, r -> {
                            double zz = size.get() - r.getSize().getHeight();
                            butt.getDisabled().setElem(zz > 0);
                            return r.withStart(r.getStart()
                                    .add(new Point(0, Math.min(0, zz * slider.getNow().get()))));
                        }))
                                .apply(clippedDrawable -> size = new ChangeableDrawable.ChangeableValue<>(clippedDrawable, 0.0))
                                .makeAspect(false,1, -1, i -> (double) i.getChild().get().size())
                                .makeClipped(true, c -> c.withSize(new Size(c.getSize().getWidth(), 9000000)))
                                .makeClipped(true, r -> new Rectangle(r.getStart(),
                                    new Size(r.getSize().getWidth(), size.setElem(r.getSize().getHeight()).get()))).get(),
                        Builder.make(new Button<>(new CheckboxClickable(), null, null, null))
                                .apply(b -> settingIsOn = b.getChild().get().getOn())
                                .makeMargin(5)
                                .get())
                        .get(),
                slider)
                .makeBackgrounded(ColorScheme.getCurrentColorScheme().backgroundColor)
                .get();

        IDrawable gameViewer = Builder.make(new GameViewer(() -> {
                    Integer res = radioButtons.getSelected().get();
                    if (res == null || res == -1) {
                        return null;
                    }
                    return radioButtons.getButton(res).get().getData();
                }))
                .makeBackgrounded(ColorScheme.getCurrentColorScheme().backgroundColor)
                .get();

        main = Builder.make(
                new Splitter(true, () -> butt.getDisabled().get() ? -50.0 : -80.0, gameViewer, menu)).get();
    }

    public static String formatString(String format, String... others) {
        for(int i = 0; i < others.length; ++i) {

            int index = format.indexOf("%s");
            int index2 = format.indexOf("%0.s");
            if(index2 != -1 && index2 < index) {
                format = format.substring(0, index2) + format.substring(index2 + 4);
                continue;
            }

            if(index == -1) {
                return format;
            }
            format = format.substring(0, index) + others[i] + format.substring(index + 2);
        }
        return format;
    }

    @Override
    public Game init() {
        if(SystemSpecific.get().getArgs().length < 2) {
            SystemSpecific.get().setArgs(new String[] {
                    "/game/inc/process/p.gameLoad.php", // "."
                    "process=%s&level=%s", // "%s.txt"
                    "img/"
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

        for(int i = 0; i < 10; ++i) {
            SystemSpecific.get().preLoad(Parameters.imgDir() + "q" + i + ".png", true);
            SystemSpecific.get().preLoad(Parameters.imgDir() + "b" + i + ".png", true);
            SystemSpecific.get().preLoad(Parameters.imgDir() + "f" + i + ".png", true);
        }
        for(int i = 1; i < 16; ++i) {
            SystemSpecific.get().preLoad(Parameters.imgDir() + i + ".jpg", true);
        }
        SystemSpecific.get().preLoad(Parameters.imgDir() + "m_on.png", true);
        SystemSpecific.get().preLoad(Parameters.imgDir() + "m_off.png", true);

        readReachableFiles();
        return this;
    }

    public void readReachableFiles() {
        if(SystemSpecific.get().getArgs().length < 2) {
            SystemSpecific.get().log("No file argument");
            return;
        }
        String from = SystemSpecific.get().getArgs()[0];
        String file = formatString(SystemSpecific.get().getArgs()[1], "1", "0");
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

        String selected = null;
        int selectedIndex = radioButtons.getSelected().get();
        if(maxButton > 0 && selectedIndex >= 0) {
            selected = radioButtons.getButton(selectedIndex).get().getData();
        }

        for (; i < Math.min(maxButton, maxFiles); ++i) {
            ButtonWithData<ColorSchemeClickable, String> button = radioButtons.getButton(i).get();
            button.setData(fileNames[i]);

            if(selected != null && selected.equals(fileNames[i])) {
                button.getChild().get().getOn().setElem(true);
                button.getAtClick().get().accept(button);
            }
        }
        for(; i < maxButton; ++i) {
            radioButtons.getButton(i).get().setData(null);
        }

        for(; i < maxFiles; ++i) {
            ButtonWithData<ColorSchemeClickable, String> button = new ButtonWithData<>(new ColorSchemeClickable(true, true), null, null, null, fileNames[i]);

            radioButtons.addButton(button);

            if(selected != null && selected.equals(fileNames[i])) {
                button.getChild().get().getOn().setElem(true);
                button.getAtClick().get().accept(button);
            }
        }
        SystemSpecific.get().runAfter(() -> readReachableFiles(), 60000);
    }

    @Override
    public ValueOrSupplier<IDrawable> getMain() {
        return new ValueOrSupplier<>(main);
    }
}
