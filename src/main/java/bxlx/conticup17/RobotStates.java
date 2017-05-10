package bxlx.conticup17;

import bxlx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by qqcs on 5/8/17.
 */
public class RobotStates {
    public final static class StationOutputType {
        public final static StationOutputType GOOD = new StationOutputType(Color.GREEN);
        public final static StationOutputType BAD = new StationOutputType(Color.RED);
        public final static StationOutputType TO = new StationOutputType(Color.CYAN);
        public final static StationOutputType FROM = new StationOutputType(Color.LIGHT_GRAY);

        private final Color color;

        private StationOutputType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    public final static class StationType {
        public final static StationType CONVEYOR_IN = new StationType(StationOutputType.FROM, StationOutputType.FROM, StationOutputType.FROM, StationOutputType.FROM, StationOutputType.FROM);
        public final static StationType WORKSTATION = new StationType(StationOutputType.GOOD,
                StationOutputType.TO,
                StationOutputType.BAD);
        public final static StationType PUFFER = new StationType(StationOutputType.TO);
        public final static StationType REPAIR = new StationType(StationOutputType.FROM,
                StationOutputType.TO);
        public final static StationType CONVEYOR_OUT = new StationType(StationOutputType.GOOD,
                     StationOutputType.BAD);

        private final List<StationOutputType> outputs = new ArrayList<>();

        private StationType(StationOutputType... stationOutputTypes) {
            for(int i = 0; i < stationOutputTypes.length; ++i) {
                outputs.add(stationOutputTypes[i]);
            }
        }

        public List<StationOutputType> getOutputs() {
            return outputs;
        }
    }

    public final static class Station {
        private final StationType type;
        private final String name;

        public Station(StationType type, String name) {
            this.type = type;
            this.name = name;
        }

        public StationType getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    public final static class RobotPlayer {
        private final String name;
        private final List<Station> stations;

        RobotPlayer(String name, List<Station> stations) {
            this.name = name;
            this.stations = stations;
        }

        public String getName() {
            return name;
        }

        public List<Station> getStations() {
            return stations;
        }
    }

    private final List<RobotPlayer> players = new ArrayList<>();


    public RobotStates(String s) {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(StationType.CONVEYOR_IN, "CON"));
        stations.add(new Station(StationType.WORKSTATION, "S1"));
        stations.add(new Station(StationType.WORKSTATION, "S2"));
        stations.add(new Station(StationType.WORKSTATION, "S3"));
        stations.add(new Station(StationType.WORKSTATION, "S4"));
        stations.add(new Station(StationType.CONVEYOR_OUT, "END"));
        stations.add(new Station(StationType.PUFFER, "P"));
        stations.add(new Station(StationType.REPAIR, "R"));

        players.add(new RobotPlayer("test1", stations));

        stations = new ArrayList<>();
        stations.add(new Station(StationType.WORKSTATION, "S3"));
        stations.add(new Station(StationType.WORKSTATION, "S2"));
        stations.add(new Station(StationType.WORKSTATION, "S1"));
        stations.add(new Station(StationType.CONVEYOR_IN, "CON"));
        stations.add(new Station(StationType.REPAIR, "R"));
        stations.add(new Station(StationType.PUFFER, "P"));
        stations.add(new Station(StationType.CONVEYOR_OUT, "END"));
        stations.add(new Station(StationType.WORKSTATION, "S4"));

        players.add(new RobotPlayer("test2", stations));

    }

    public List<RobotPlayer> getPlayers() {
        return players;
    }
}
