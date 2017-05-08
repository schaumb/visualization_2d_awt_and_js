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
    public enum StationOutputType {
        GOOD(Color.GREEN),
        BAD(Color.RED),
        TO(Color.CYAN),
        FROM(Color.LIGHT_GRAY);

        private final Color color;

        StationOutputType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    public enum StationType {
        CONVEYOR_IN(5, StationOutputType.FROM),
        WORKSTATION(StationOutputType.GOOD,
                StationOutputType.FROM,
                StationOutputType.BAD),
        PUFFER(StationOutputType.TO),
        REPAIR(StationOutputType.FROM,
                StationOutputType.TO),
        CONVEYOR_OUT(StationOutputType.GOOD,
                     StationOutputType.BAD);

        private final List<StationOutputType> outputs;

        StationType(int stationCount, StationOutputType stationOutputType) {
            outputs = Collections.nCopies(stationCount, stationOutputType);
        }

        StationType(StationOutputType... stationOutputTypes) {
            outputs = Arrays.asList(stationOutputTypes);
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
