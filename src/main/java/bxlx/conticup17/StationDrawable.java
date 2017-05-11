package bxlx.conticup17;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 5/8/17.
 */
public class StationDrawable extends Container<IDrawable> {

    public StationDrawable(Direction direction, RobotStates states, int playerNum, int stationNum) {
        super();

        RobotStates.StationType station = states.getState().getPlayers()[playerNum].getStations().get(stationNum);
        if(station == null || station.isOutDrew())
            return;

        boolean x = direction.getVector().getX() == 0;
        SplitContainer<IDrawable> outerContainer = new SplitContainer<>(!x);
        for(int i = 0; i < 5; ++i) {
            outerContainer.add((IDrawable) null);
        }

        SplitContainer<IDrawable> innerContainer = new SplitContainer<>(x);

        int outputSize = station.getOutputs().size();
        for(int i = 0; i < outputSize; ++i) {
            RobotStates.StationOutputType outp = station.getOutputs().get(((stationNum + 1) % 8 > 3) == (playerNum == 0) ? outputSize - 1 - i : i);

            if(outp == null) {
                innerContainer.add((IDrawable) null);
                continue;
            }
            innerContainer.add(Builder.container()
                    .add(new AspectRatioDrawable<>(new PacketDrawable(() -> {
                        if(states.getState() == null)
                            return null;
                        RobotStates.StationType stationX = states.getState().getPlayers()[playerNum].getStations().get(stationNum);
                        RobotStates.Unit unit = states.getState().getUnits().get(stationX.getString(
                                states.getState().getPlayers()[playerNum].getName()
                        ) + "-" + outp.getAbbr());
                        return unit;
                    }), false,  (int) -direction.getVector().getX(), (int) -direction.getVector().getY(), 1.0))
                    .makeBackgrounded(outp.getColor())
                    .makeMargin(
                            x ? 0.1 : 0,
                            x ? 0 : 0.1)
                    .makeBackgrounded(Color.DARK_GRAY).makeMargin(0.2).get());
        }

        outerContainer.get(direction.getVector().asSize().getLongerDimension() == 0 ? 0 : outerContainer.size() - 1)
                .setElem(innerContainer);


        add(Builder.container()
                .add(Builder.background().makeColored(Color.WHITE).makeMargin(0.1/5)
                        .makeBackgrounded(Color.DARK_GRAY).makeMargin(0.1).get())
                .add(Builder.make(outerContainer)
                    .makeMargin(
                            x ? 0.1 : 0,
                            x ? 0 : 0.1
                    ).get())
                .add(Builder.text(station.getAbbr(), "CON", 0)
                        .makeColored(ColorScheme.getCurrentColorScheme().textColor)
                        .makeAspect(0, 0, 1)
                        .makeMargin(0.5).get())
                .get());
    }
}
