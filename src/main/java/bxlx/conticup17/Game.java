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

    private final ValueOrSupplier<RobotStates> states = new ValueOrSupplier<>((RobotStates) null);
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

        tryToReadStates();

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
                ".", "tester.txt"
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
                system.log("Can not reach file: " + system.getArgs()[0] + " ? " + system.getArgs()[1] + ", try again after 10 sec");
            }
            system.runAfter(() -> tryToReadStates(), 10000);

            return;
        }

        main = new Builder.ContainerBuilder<>(new StateDrawable(new RobotStates(s)))
                .makeAspect(-1, -1, 0.5)
                .makeBackgrounded(ColorScheme.getCurrentColorScheme().backgroundColor)
                .get();
    }
}
