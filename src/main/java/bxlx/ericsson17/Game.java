package bxlx.ericsson17;

import bxlx.general.IGame;
import bxlx.graphics.Color;
import bxlx.graphics.Font;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.drawable.TimerDrawable;
import bxlx.graphics.fill.MultilineText;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;

/**
 * Created by qqcs on 5/8/17.
 */
public class Game implements IGame {

    private final TimerDrawable timer = new TimerDrawable(1700);
    private final TimerDrawable dotTimer = new TimerDrawable(2500);
    private final MultilineText text = new MultilineText(() -> "Connecting " + (dotTimer.percent() > 0.25 ? "." : " ")
     + (dotTimer.percent() > 0.5 ? "." : " ") + (dotTimer.percent() > 0.75 ? "." : " "));

    private final int balls = 12;
    private final IDrawable wait = Builder.container(true)
            .fillN(balls, i -> Builder.circle(true)
                    .makeColored(Color.BLACK)
                    .makeClipped(true, c -> c.withStart(c.getStart().add(new Point(0,
                            Math.min(0, Math.sin((timer.percent() - i / (balls + 0.0) ) * Math.PI * 2)) * (c.getSize().getHeight() - c.getSize().getWidth()) / 2))))
                    .get())
            .toContainer(0)
            .add(timer)
            .add(Builder.container(false)
                    .add(dotTimer)
                    .add(Builder.make(text).makeColored(Color.GREEN)
                            .makeClipped(c -> c.getScaled(0.5)).get()).get()
            )
            .makeAspect(-1, -1, 0.5)
            .makeBackgrounded(ColorScheme.getCurrentColorScheme().backgroundColor)
            .get();

    private ValueOrSupplier<IDrawable> main = new ValueOrSupplier<>(() -> wait);

    private final int maxTries = 9;
    private int tries = 0;

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

        SystemSpecific.get().runAfter(() -> tryToReadStates(), 0);

        return this;
    }

    @Override
    public ValueOrSupplier<IDrawable> getMain() {
        return main;
    }

    public void tryToReadStates() {
        SystemSpecific system = SystemSpecific.get();

        String[] args = system.getArgs();

        if(args == null || args.length < 2) {
            system.setArgs(args = new String[] {
                "./", "0.txt"
            });
        }

        system.readTextFileAsync(args[0], args[1], s -> gotStates(s));

    }

    public void gotStates(String s) {
        SystemSpecific system = SystemSpecific.get();

        if(s == null || s.trim().isEmpty()) {
            String[] args = system.getArgs();
            if(args == null || args.length < 2) {
                system.log("Not set argument - cannot known which file to download");
                text.getText().setElem("Not set arguments - inner error");
            } else {
                text.getText().setElem("Can not reach the game. \nAttempts: " + ++tries + " / " + maxTries);
                if(tries != maxTries) {
                    system.log("Can not reach file: " + system.getArgs()[0] + " ? " + system.getArgs()[1] + ", try again after 2 sec");
                } else {
                    timer.stop();
                    return;
                }
            }
            system.runAfter(() -> tryToReadStates(), 2000);

            return;
        }
        // SystemSpecific.get().log("Loaded data: " + Arrays.toString(SystemSpecific.get().getArgs()) + " -- " + s);
            text.getText().setSupplier(() -> "Loading " + (dotTimer.percent() > 0.25 ? "." : " ")
                    + (dotTimer.percent() > 0.5 ? "." : " ") + (dotTimer.percent() > 0.75 ? "." : " "));
            system.runAfter(() ->  {
                try {
                    new GameLogic(s, main, text.getText()).run();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    text.getText().setElem("ERROR: " + throwable.getMessage());
                    timer.stop();
                }
            }, 0);
    }
}
