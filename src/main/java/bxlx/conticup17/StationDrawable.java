package bxlx.conticup17;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.SplitContainer;
import bxlx.system.ColorScheme;

/**
 * Created by qqcs on 5/8/17.
 */
public class StationDrawable extends Container<IDrawable> {

    public StationDrawable(Direction direction, RobotStates.Station station) {
        super();

        boolean x = direction.getVector().getX() == 0;
        SplitContainer<IDrawable> outerContainer = new SplitContainer<>(!x);
        for(int i = 0; i < 5; ++i) {
            outerContainer.add((IDrawable) null);
        }

        SplitContainer<IDrawable> innerContainer = new SplitContainer<>(x);

        int outputSize = station.getType().getOutputs().size();
        if (outputSize < 4) {
            innerContainer.add((IDrawable) null);
        }
        if (outputSize < 2) {
            innerContainer.add((IDrawable) null);
        }
        for(int i = 0; i < outputSize; ++i) {
            if(outputSize % 2 == 0 && outputSize / 2 == i) {
                innerContainer.add((IDrawable) null);
            }

            innerContainer.add(Builder.background().makeColored(station.getType().getOutputs().get(i).getColor())
                    .makeMargin(
                            x ? 2 : 0,
                            x ? 0 : 2)
                    .makeBackgrounded(Color.DARK_GRAY).makeMargin(3).get());
        }
        if (outputSize < 4) {
            innerContainer.add((IDrawable) null);
        }
        if (outputSize < 2) {
            innerContainer.add((IDrawable) null);
        }

        outerContainer.get(direction.getVector().asSize().getLongerDimension() == 0 ? 0 : outerContainer.size() - 1)
                .setElem(innerContainer);


        add(Builder.container()
                .add(Builder.background().makeColored(Color.WHITE).makeMargin(2)
                        .makeBackgrounded(Color.DARK_GRAY).makeMargin(10).get())
                .add(Builder.make(outerContainer)
                    .makeMargin(
                            x ? 7 : 0,
                            x ? 0 : 7
                    ).get())
                .add(Builder.text(station.getName(), "CON", 0)
                        .makeColored(ColorScheme.getCurrentColorScheme().textColor)
                        .makeAspect(0, 0, 1)
                        .makeMargin(0.5).get())
                .get());
    }
}
