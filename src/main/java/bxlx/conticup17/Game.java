package bxlx.conticup17;

import bxlx.general.IGame;
import bxlx.graphics.Color;
import bxlx.graphics.Font;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;

/**
 * Created by qqcs on 5/8/17.
 */
public class Game implements IGame {

    private final IDrawable wait = Builder.container(true)
            .add(Builder.background().makeColored(Color.GREEN).get())
            .add(Builder.background().makeColored(Color.RED).get())
            .makeAspect(-1, -1, 0.5)
            .makeBackgrounded(ColorScheme.getCurrentColorScheme().backgroundColor)
            .get();

    private IDrawable main = wait;

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
        return new ValueOrSupplier<>(() -> main);
    }

    public void tryToReadStates() {
        SystemSpecific system = SystemSpecific.get();

        String[] args = system.getArgs();

        if(args == null || args.length < 2) {
            system.setArgs(args = new String[] {
                "./", "log.log"
            });
        }

        system.readTextFileAsync(args[0], args[1], s -> gotStates(s));

    }

    public void gotStates(String s) {

        if(s == null || s.trim().isEmpty()) {
            SystemSpecific system = SystemSpecific.get();
            String[] args = system.getArgs();
            if(args == null || args.length < 2) {
                system.log("Not set argument - cannot known which file to download");
            } else {
                system.log("Can not reach file: " + system.getArgs()[0] + " ? " + system.getArgs()[1] + ", try again after 2 sec");
            }
            system.runAfter(() -> tryToReadStates(), 2000);

            return;
        }

        try {
            RobotStates states = new RobotStates(s);
            RobotStateTimer timer = new RobotStateTimer(states);
            main = new Builder.ContainerBuilder<>(new StateDrawable(states, timer))
                    .makeAspect(-1, -1, 0.5)
                    .makeBackgrounded(ColorScheme.getCurrentColorScheme().backgroundColor)
                    .add(timer)
                    .get();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
