package bxlx.conticup17;

import bxlx.graphics.Direction;
import bxlx.graphics.IDrawable;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.container.Splitter;
import bxlx.system.functional.ValueOrSupplier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 5/8/17.
 */
public class OneStateDrawable extends Container<IDrawable> {
    private final List<ValueOrSupplier<IDrawable>> stations = new ArrayList<>();

    public OneStateDrawable(RobotStates states, RobotStateTimer timer, int playerNum) {
        super();
        List<RobotStates.StationType> stateStations = states.getState().getPlayers()[playerNum].getStations();

        double centerSize = 0.5;
        for(int i = 0; i < stateStations.size(); ++i) {
            stations.add(new ValueOrSupplier<>((IDrawable) null));
        }


        double separate1 = (1 + centerSize) / -2;
        double separate2 = 2 * centerSize / (1 + centerSize);

        Splitter otherStationHolder = Splitter.threeWaySplit(false, centerSize,
                new Splitter(true, separate1, null,
                        new Splitter(true, separate2, new SplitContainer<>(true)
                                .add(stations.get(2).getAsSupplier())
                                .add(stations.get(1).getAsSupplier()), null)),
                Splitter.threeWaySplit(true, centerSize,
                        new SplitContainer<>(false)
                                .add(stations.get(3).getAsSupplier())
                                .add(stations.get(4).getAsSupplier()),

                        null,
                        new SplitContainer<>(false)
                                .add(stations.get(0).getAsSupplier())
                                .add(stations.get(7).getAsSupplier())),
                Splitter.threeWaySplit(true, centerSize, null,
                        new SplitContainer<>(true)
                                .add(stations.get(5).getAsSupplier())
                                .add(stations.get(6).getAsSupplier()),
                        null));

        int maxStations = Math.min(stations.size(), stateStations.size());
        for(int i = 0; i < maxStations; ++i) {
            Direction direction = Direction.UP;
            switch ((i + 1) % maxStations / 2) {
                case 0:
                    direction = Direction.LEFT;
                    break;
                case 1:
                    direction = Direction.DOWN;
                    break;
                case 2:
                    direction = Direction.RIGHT;
                    break;
            }

            Direction effFinal = direction;
            int iTh = i;
            stations.get(i).setElem(new StationDrawable(effFinal, states, playerNum, iTh));
        }

        add(otherStationHolder);

        add(new RobotDrawable(states, timer, playerNum));
    }



}
