package bxlx.conticup17;

import bxlx.graphics.Color;
import bxlx.graphics.Point;
import bxlx.system.SystemSpecific;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qqcs on 5/8/17.
 */
public class RobotStates {
    public final static class Unit implements Cloneable {
        private final String id;
        private final String type;
        private final Color typeColor;

        public Unit(String id, String type) {
            this.id = id;
            this.type = type;

            switch (type) {
                case "ERC1":
                    typeColor = Color.MAGENTA.getScale(Color.WHITE, 0.5).getScale(Color.OPAQUE, 0.1);
                    break;
                default:
                    typeColor = Color.BLUE.getScale(Color.WHITE, 0.5).getScale(Color.OPAQUE, 0.1);
                    break;
            }
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public Color getTypeColor() {
            return typeColor;
        }

        @Override
        public String toString() {
            return "Unit{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    public final static class StationOutputType {
        public final static StationOutputType PASS = new StationOutputType("PASS", Color.GREEN);
        public final static StationOutputType FAIL = new StationOutputType("FAIL", Color.RED);
        public final static StationOutputType WORK = new StationOutputType("WORK", Color.CYAN);
        public final static StationOutputType[] POS = new StationOutputType[] {
                new StationOutputType("POS_0", Color.LIGHT_GRAY),
                new StationOutputType("POS_1", Color.LIGHT_GRAY),
                new StationOutputType("POS_2", Color.LIGHT_GRAY),
                new StationOutputType("POS_3", Color.LIGHT_GRAY),
                new StationOutputType("POS_4", Color.LIGHT_GRAY),
        };
        public final static StationOutputType PASS_GRAY =
                new StationOutputType("PASS", Color.LIGHT_GRAY);

        private final String abbr;
        private final Color color;

        private StationOutputType(String abbr, Color color) {
            this.abbr = abbr;
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public String getAbbr() {
            return abbr;
        }

        @Override
        public String toString() {
            return "StationOutputType{" +
                    "abbr='" + abbr + '\'' +
                    ", color=" + color +
                    '}';
        }
    }

    public final static class StationType {
        public final static StationType CONVEYOR_IN = new StationType(0, "CON",
                StationOutputType.POS[0],
                StationOutputType.POS[1],
                StationOutputType.POS[2],
                StationOutputType.POS[3],
                StationOutputType.POS[4]);
        public final static StationType[] WORKSTATIONS = new StationType[]{
                new StationType(1, "S1", null, StationOutputType.PASS, StationOutputType.WORK, StationOutputType.FAIL, null),
                new StationType(2, "S2", null, StationOutputType.PASS, StationOutputType.WORK, StationOutputType.FAIL, null),
                new StationType(3, "S3", null, StationOutputType.PASS, StationOutputType.WORK, StationOutputType.FAIL, null),
                new StationType(4, "S4", null, StationOutputType.PASS, StationOutputType.WORK, StationOutputType.FAIL, null)
        };
        public final static StationType REWORK = new StationType(5, "RW", null, StationOutputType.PASS_GRAY, null, StationOutputType.WORK, null);
        public final static StationType CONVEYOR_OUT = new StationType(6, "END", null, StationOutputType.PASS, null, StationOutputType.FAIL, null);

        private final List<StationOutputType> outputs = new ArrayList<>();
        private final int id;
        private final String abbr;

        private StationType(int id, String abbr, StationOutputType... stationOutputTypes) {
            this.id = id;
            this.abbr = abbr;
            for(int i = 0; i < stationOutputTypes.length; ++i) {
                outputs.add(stationOutputTypes[i]);
            }
        }

        public List<StationOutputType> getOutputs() {
            return outputs;
        }

        public boolean isOutDrew() {
            return this == CONVEYOR_IN;
        }

        public String getAbbr() {
            return abbr;
        }

        public int getId() {
            return id;
        }

        public String getString(String playerName) {
            return (this != CONVEYOR_IN ? playerName + "-" : "") + getAbbr();
        }

        @Override
        public String toString() {
            return "StationType{" +
                    "outputs=" + outputs +
                    ", id=" + id +
                    ", abbr='" + abbr + '\'' +
                    '}';
        }
    }

    public final static class RobotPlayer implements Cloneable {
        private final int index;
        private boolean identified = false;
        private String name = "";
        private Unit ownedUnit = null;
        private Unit nextUnit = null;
        private String nextUnitFrom = null;
        private boolean prevPutted = false;
        private Point from;
        private Point to;
        private String scores = "PASS: 0,0;FAIL: 0,0";
        private String errorMessage = null;
        private final List<StationType> stations;

        public RobotPlayer(int index, Point startPos, List<StationType> stations) {
            this.index = index;
            this.from = this.to = startPos;
            this.stations = stations;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<StationType> getStations() {
            return stations;
        }

        public Unit getOwnedUnit() {
            return ownedUnit;
        }

        public void setOwnedUnit(Unit ownedUnit) {
            this.ownedUnit = ownedUnit;
        }

        public Unit getNextUnit() {
            return nextUnit;
        }

        public String getNextUnitFrom() {
            return nextUnitFrom;
        }

        public void setNextUnit(Unit nextUnit, String from) {
            this.nextUnit = nextUnit;
            this.nextUnitFrom = from;
        }

        public boolean isPrevPutted() {
            return prevPutted;
        }

        public void setPrevPutted(boolean prevPutted) {
            this.prevPutted = prevPutted;
        }

        public boolean isIdentified() {
            return identified;
        }

        public void setIdentified(boolean identified) {
            this.identified = identified;
        }

        public Point getFrom() {
            return from;
        }

        public void setFrom(Point from) {
            this.from = from;
        }

        public Point getTo() {
            return to;
        }

        public void setTo(Point to) {
            this.to = to;
        }

        public String getScores() {
            return scores;
        }

        public void setScores(String scores) {
            this.scores = scores;
        }

        public int getIndex() {
            return index;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public RobotPlayer clone() {
            RobotPlayer cloned = new RobotPlayer(index, from, stations);
            cloned.setIdentified(identified);
            cloned.setName(name);
            cloned.setOwnedUnit(ownedUnit);
            cloned.setFrom(from);
            cloned.setTo(to);
            cloned.setScores(scores);
            cloned.setErrorMessage(errorMessage);
            cloned.setNextUnit(nextUnit, nextUnitFrom);
            //cloned.setPrevPutted(prevPutted);
            return cloned;
        }

        @Override
        public String toString() {
            return "RobotPlayer{" +
                    "index=" + index +
                    ", identified=" + identified +
                    ", name='" + name + '\'' +
                    ", ownedUnit=" + ownedUnit +
                    ", from=" + from +
                    ", to=" + to +
                    ", scores='" + scores + '\'' +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", stations=" + stations +
                    '}';
        }
    }

    public static final class WholeState implements Cloneable {
        private final RobotPlayer[] players;
        private final HashMap<String, Unit> units;
        private final List<Unit> changedTo = new ArrayList<>();
        private final List<Integer> changedFromPlayerIndex = new ArrayList<>();
        private final List<Unit> changedFrom = new ArrayList<>();
        private List<String[]> switchUnits = new ArrayList<>();
        private String command = "";
        private boolean changedName = true;

        private WholeState(RobotPlayer[] players, HashMap<String, Unit> units) {
            this.players = new RobotPlayer[] {
                    players[0].clone(),
                    players[1].clone()
            };
            this.units = new HashMap<>(units);
        }
        public WholeState() {
            this.players = new RobotPlayer[] {
                    new RobotPlayer(0, new Point(2, -1), Arrays.asList(
                            StationType.CONVEYOR_IN,
                            null,
                            StationType.WORKSTATIONS[0],
                            StationType.WORKSTATIONS[1],
                            StationType.WORKSTATIONS[2],
                            StationType.WORKSTATIONS[3],
                            StationType.CONVEYOR_OUT,
                            StationType.REWORK
                    )),
                    new RobotPlayer(1, new Point(1, -1), Arrays.asList(
                            StationType.WORKSTATIONS[1],
                            StationType.WORKSTATIONS[0],
                            null,
                            StationType.CONVEYOR_IN,
                            StationType.REWORK,
                            StationType.CONVEYOR_OUT,
                            StationType.WORKSTATIONS[3],
                            StationType.WORKSTATIONS[2]
                    ))
            };
            this.units = new HashMap<>();

        }

        @Override
        public WholeState clone() {
            WholeState result = new WholeState(players, units);
            for(String[] switchableUnits : switchUnits) {
                result.switchUnits(switchableUnits[0], switchableUnits[1]);
            }
            return result;
        }

        public RobotPlayer[] getPlayers() {
            return players;
        }

        public HashMap<String, Unit> getUnits() {
            return units;
        }

        @Override
        public String toString() {
            return "WholeState{" +
                    "players=" + Arrays.toString(players) +
                    ", units=" + units +
                    '}';
        }

        public void switchUnits(String unitKeyFrom, String unitKeyTo) {
            switchUnits.add(new String[]{unitKeyFrom, unitKeyTo});
        }

        public String hasSwitchUnit(Unit unit) {
            for(String[] switchUnit : switchUnits) {
                if(units.get(switchUnit[0]) == unit)
                    return switchUnit[1];
            }
            return null;
        }

        public void makeSwitch() {
            List<String[]> switchUnitNotReady = new ArrayList<>();
            for(String[] switchableUnits : switchUnits) {
                Unit toUnit = units.get(switchableUnits[0]);
                if(toUnit == null) {
                    switchUnitNotReady.add(switchableUnits);
                } else {
                    units.put(switchableUnits[0], units.get(switchableUnits[1]));
                    units.put(switchableUnits[1], toUnit);
                }
            }
            switchUnits = switchUnitNotReady;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public boolean isChangedName() {
            return changedName;
        }

        public void setChangedName() {
            this.changedName = true;
        }

        public void addChangedTo(Unit u) {
            changedTo.add(u);
        }

        public void addChangedFrom(Unit u, int playerIndex) {
            changedFrom.add(u);
            changedFromPlayerIndex.add(playerIndex);
        }

        public List<Unit> getChangedTo() {
            return changedTo;
        }

        public List<Unit> getChangedFrom() {
            return changedFrom;
        }

        public List<Integer> getChangedFromPlayerIndex() {
            return changedFromPlayerIndex;
        }
    }

    private final Map<Integer, WholeState> timeStates = new HashMap<>();
    private WholeState currentState;
    private int time = 0;

    public boolean setNextState() {
        int closestBigger = -1;
        for(Map.Entry<Integer, WholeState> entry : timeStates.entrySet()) {
            if(entry.getKey() > time && closestBigger < time) {
                closestBigger = entry.getKey();
            } else if(entry.getKey() > time && entry.getKey() < closestBigger) {
                closestBigger = entry.getKey();
            }
        }
        currentState = timeStates.get(time);
        time = closestBigger;
        return closestBigger != -1;

    }

    public WholeState getState() {
        if(currentState == null) {
            int closestBigger = -1;
            for (Map.Entry<Integer, WholeState> entry : timeStates.entrySet()) {
                if (entry.getKey() >= time && closestBigger < time) {
                    closestBigger = entry.getKey();
                } else if (entry.getKey() >= time && entry.getKey() < closestBigger) {
                    closestBigger = entry.getKey();
                }
            }
            return currentState = timeStates.get(closestBigger);
        } else {
            return currentState;
        }
    }

    public int getTime() {
        return time;
    }


    public RobotStates(String s) {
        String[] lines = s.split("\n");

        WholeState state = new WholeState();
        timeStates.put(0, state);

        long startTime = -1000;

        HashMap<String, String> incomings = new HashMap<>(); // Robot_id,Message_id -> time,command,oth
        for(String line : lines) {
            if((line = line.trim()).isEmpty()) {
                continue;
            }

            String[] infos = new String[] {
                    line.substring(0, line.indexOf("|")),
                    line.substring(line.indexOf("|") + 1, line.lastIndexOf("|")),
                    line.substring(line.lastIndexOf("|") + 1)
            };

            String[] time = infos[0].split(":");

            long timestamp =
                    Long.parseLong(time[0].substring(time[0].indexOf(" ") + 1, time[0].length())) * 60 * 60 * 1000 +
                    Long.parseLong(time[1]) * 60 * 1000 +
                    Long.parseLong(time[2].substring(0, time[2].indexOf("."))) * 1000 +
                    Long.parseLong(time[2].substring(time[2].indexOf(".") + 1));
            if(startTime == -1000) {
                startTime = timestamp - 100;
            }

            String player = infos[1];
            String message = infos[2];

            boolean incoming = message.contains("INCOMING");

            message = message.substring(incoming ? 11 : 12, message.length() - 1);

            if(message.charAt(0) == '<') {
                message = message.substring(5, message.length() - 5);
            } else {
                for(int i = 0; i < message.length(); ++i) {
                    if('A' <= message.charAt(i) && message.charAt(i) <= 'Z') {
                        message = message.substring(i, message.length() - i);
                        break;
                    }
                }
            }

            String[] messagePieces = message.split(",");

            if(incoming) {
                String key = messagePieces[1] + "," + messagePieces[2];
                String value = timestamp + "," + messagePieces[0];

                for(int i = 3; i < messagePieces.length; ++i) {
                    value += "," + messagePieces[i];
                }

                String prev = incomings.get(key);

                if(prev != null) {
                    SystemSpecific.get().log("WARNING - MALFORMED LOG - " + infos[0] + " MORE THAN 1 KEY AT THE SAME TIME OF: " + key + " IGNORING --- " + timestamp + "," + message + " --- " + prev);
                } else {
                    incomings.put(key, value);
                }
            } else {
                String key = player + "," + messagePieces[1];

                String prev = incomings.get(key);

                switch (messagePieces[0]) {
                    case "IDENTIFICATION":
                    case "MOVE":
                    case "PICK":
                    case "PLACE":
                    case "UNIT_INFO":
                    case "ERROR":
                        if(prev == null) {
                            SystemSpecific.get().log("WARNING - MALFORMED LOG - " + infos[0] + " NO KEY FOR THIS MESSAGE: " + timestamp + "," + message + " IGNORING");
                            continue;
                        }
                        break;
                }
                boolean wasNew = false;
                switch (messagePieces[0]) {
                    case "MOVE":
                    case "PICK":
                    case "PLACE":
                    case "STATION_READY":
                    case "UNITS_ON_CONVEYOR":
                    case "UNITS_ON_STATION":
                    case "QTY_INFO":
                        state = state.clone();
                        wasNew = true;

                        for(RobotPlayer robotPlayer : state.getPlayers()) {
                            String from = robotPlayer.getNextUnitFrom();
                            Unit unit = robotPlayer.getNextUnit();

                            robotPlayer.setFrom(robotPlayer.getTo());

                            if(from == null || unit == robotPlayer.getOwnedUnit())
                                continue;

                            if(unit == null) {
                                state.getUnits().put(from, robotPlayer.getOwnedUnit());
                                robotPlayer.setPrevPutted(true);
                                state.addChangedTo(robotPlayer.getOwnedUnit());
                            } else {
                                state.getUnits().remove(from);
                            }
                            robotPlayer.setOwnedUnit(robotPlayer.getNextUnit());
                        }

                        state.makeSwitch();
                        state.setCommand(state.getCommand() + " ++ " + message);

                    break;
                    default:
                        state.setCommand(state.getCommand() + " ++ " + message);
                        break;
                }

                if(prev != null) {
                    String[] prevMsgs = prev.split(",");

                    switch (messagePieces[0]) {
                        case "IDENTIFICATION":
                            for(RobotPlayer robotPlayer : state.getPlayers()) {
                                if(robotPlayer.getName().isEmpty()) {
                                    robotPlayer.setName(player);
                                    robotPlayer.setIdentified(true);
                                    state.setChangedName();
                                    break;
                                } else if(robotPlayer.getName().equals(player)) {
                                    break;
                                }
                            }
                            break;
                        case "MOVE":
                            for(RobotPlayer robotPlayer : state.getPlayers()) {
                                if (robotPlayer.getName().equals(player)) {
                                    String to = prevMsgs[3];
                                    for(int i = 0; i < robotPlayer.getStations().size(); ++i) {
                                        StationType stationType = robotPlayer.getStations().get(i);
                                        if(stationType != null && stationType.getAbbr().equals(to)) {
                                            robotPlayer.setTo(new Point(i, -1));
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        case "PICK":
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (robotPlayer.getName().equals(player)) {
                                    String pos = prevMsgs[2];
                                    int stationId = (int) robotPlayer.getFrom().getX();
                                    StationType stationType = robotPlayer.getStations().get(stationId);
                                    for(int k = 0; k < stationType.getOutputs().size(); ++k) {
                                        StationOutputType stationOutputType = stationType.getOutputs().get(k);
                                        if(stationOutputType == null)
                                            continue;

                                        if(stationOutputType.getAbbr().equals(pos)) {
                                            String unitMapKey = stationType.getString(robotPlayer.getName()) + "-" + pos;
                                            Unit unit = state.getUnits().get(unitMapKey);

                                            robotPlayer.setNextUnit(unit, unitMapKey);
                                            //state.getUnits().remove(unitMapKey);
                                            state.addChangedFrom(unit, i);

                                            robotPlayer.setTo(new Point(robotPlayer.getFrom().getX(), k));
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        case "PLACE":
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (robotPlayer.getName().equals(player)) {
                                    String pos = prevMsgs[2];
                                    int stationId = (int) robotPlayer.getFrom().getX();
                                    StationType stationType = robotPlayer.getStations().get(stationId);
                                    for(int k = 0; k < stationType.getOutputs().size(); ++k) {
                                        StationOutputType stationOutputType = stationType.getOutputs().get(k);
                                        if(stationOutputType == null)
                                            continue;

                                        if(stationOutputType.getAbbr().equals(pos)) {

                                            String unitMapKey = stationType.getString(robotPlayer.getName()) + "-" + pos;
                                            //state.getUnits().put(unitMapKey, robotPlayer.getOwnedUnit());
                                            robotPlayer.setNextUnit(null, unitMapKey);

                                            robotPlayer.setFrom(robotPlayer.getTo());
                                            robotPlayer.setTo(new Point(robotPlayer.getFrom().getX(), k));
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        case "UNIT_INFO":
                            break;
                        case "ERROR":
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (robotPlayer.getName().equals(player)) {
                                    robotPlayer.setErrorMessage(messagePieces[messagePieces.length - 1]);
                                    break;
                                }
                            }
                            break;
                    }
                    incomings.remove(key);
                } else {

                    switch (messagePieces[0]) {
                        case "UNITS_ON_CONVEYOR":
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (robotPlayer.getName().equals(player)) {
                                    robotPlayer.setFrom(robotPlayer.getTo());

                                    StationType stationType = robotPlayer.getStations().get((int) robotPlayer.getFrom().getX());
                                    String[] tokens = messagePieces[1].split("\"");
                                    for(int j = 0; j < 5; ++j) {
                                        StationOutputType stationOutputType = stationType.getOutputs().get(j);
                                        String unitMapKey = stationType.getString(robotPlayer.getName()) + "-" + stationOutputType.getAbbr();

                                        if(state.getUnits().get(unitMapKey) == null) {
                                            String[] unitStrings = tokens[j * 2 + 3].split(";");

                                            state.getUnits().put(unitMapKey, new Unit(unitStrings[0], unitStrings.length > 1 ? unitStrings[1] : "-"));
                                        }
                                    }
                                    break;
                                }
                            }

                            for(Map.Entry<Integer, WholeState> stateEntry : timeStates.entrySet()) {
                                StationType stationType = StationType.CONVEYOR_IN;
                                for(int j = 0; j < 5; ++j) {
                                    StationOutputType stationOutputType = stationType.getOutputs().get(j);
                                    String unitMapKey = stationType.getString("") + "-" + stationOutputType.getAbbr();

                                    if(stateEntry.getValue().getUnits().get(unitMapKey) == null) {
                                        stateEntry.getValue().getUnits().put(unitMapKey, state.getUnits().get(unitMapKey));
                                    }
                                }
                            }
                            break;
                        case "UNITS_ON_STATION":
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (player.equals(robotPlayer.getName())) {
                                    robotPlayer.setFrom(robotPlayer.getTo());
                                    break;
                                }
                            }
                            // ...
                            break;
                        case "STATION_READY":
                            String result;
                            String station;
                            if(messagePieces.length > 3) {
                                result = messagePieces[3].toUpperCase();
                                station = messagePieces[1];
                            } else {
                                String[] quoted = messagePieces[1].split("\"");
                                result = quoted[5];
                                station = quoted[1];
                            }
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (robotPlayer.getName().equals(player)) {
                                    for(StationType stationType : robotPlayer.getStations()) {

                                        if(stationType != null && stationType.getAbbr().equals(station)) {
                                            String unitKeyFrom = stationType.getString(player) + "-WORK";
                                            String unitKeyTo = stationType.getString(player) + "-" + result;
                                            state.switchUnits(unitKeyFrom, unitKeyTo);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        case "QTY_INFO":
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (player.equals(robotPlayer.getName())) {
                                    robotPlayer.setFrom(robotPlayer.getTo());
                                    break;
                                }
                            }
                            // DIREKT NINCS BREAK
                        case "END_GAME":
                            String good;
                            String bad = "";
                            if(messagePieces[1].startsWith("PASS")) {
                                good = messagePieces[1].substring(messagePieces[1].indexOf("=") + 1) + ",0";
                                bad = messagePieces[2].substring(messagePieces[2].indexOf("=") + 1) + ",0";
                            } else {
                                good = messagePieces[1].substring(messagePieces[1].lastIndexOf("="));
                                for(int i = 2; i < messagePieces.length - 1; ++i) {
                                    good += "," + messagePieces[i].substring(messagePieces[1].lastIndexOf("="));
                                    bad += messagePieces[i].substring(
                                            messagePieces[i].indexOf("=") + 1,
                                            messagePieces[i].indexOf(" ")) + ",";

                                }
                                bad += messagePieces[messagePieces.length - 1].substring(
                                        messagePieces[messagePieces.length - 1].indexOf("=") + 1,
                                        messagePieces[messagePieces.length - 1].indexOf(" "));
                            }

                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (robotPlayer.getName().equals(player)) {
                                    robotPlayer.setScores("PASS: " + good + ";FAIL: " + bad);
                                    break;
                                }
                            }
                            break;
                    }
                }
                if(wasNew) {
                    while (timeStates.containsKey((int) timestamp)) {
                        ++timestamp;
                    }
                    timeStates.put((int) timestamp, state);
                }
            }

        }
    }
}
