package bxlx.conticup17;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
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
    private final RobotStates.RobotPlayer player;

    private final List<ValueOrSupplier<IDrawable>> stations = new ArrayList<>();

    public OneStateDrawable(RobotStates.RobotPlayer player) {
        super();
        this.player = player;

        List<RobotStates.Station> stateStations = player.getStations();

        double centerSize = 0.5;
        for(int i = 0; i < stateStations.size(); ++i) {
            stations.add(new ValueOrSupplier<>((IDrawable) null));
        }

        Splitter otherStationHolder = Splitter.threeWaySplit(false, centerSize,
                Splitter.threeWaySplit(true, centerSize, null,
                        new SplitContainer<>(true)
                            .add(stations.get(2).getAsSupplier())
                            .add(stations.get(1).getAsSupplier()),
                        null),
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

            stations.get(i).setElem(new StationDrawable(direction, stateStations.get(i)));
        }

        add(otherStationHolder);

        add(new RobotDrawable(player));
    }



}
