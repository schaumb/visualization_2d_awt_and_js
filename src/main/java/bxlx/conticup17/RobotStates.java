package bxlx.conticup17;

import bxlx.graphics.Color;
import bxlx.graphics.Point;
import bxlx.system.SystemSpecific;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by qqcs on 5/8/17.
 */
public class RobotStates {
    public final static class Unit implements Cloneable {
        private String id = "";
        private Color typeColor = Color.CONTI_COLOR;

        public Unit(String id, String type) {
            setId(id);
            setType(type);
        }

        public Unit() {

        }

        public Unit setId(String id) {
            this.id = id;
            return this;
        }

        public Unit setType(String type) {
            switch (type) {
                case "ERC1":
                    typeColor = Color.MAGENTA.getScale(Color.WHITE, 0.5);
                    break;
                default:
                    typeColor = Color.BLUE.getScale(Color.WHITE, 0.5);
                    break;
            }
            return this;
        }

        public String getId() {
            return id;
        }

        public Color getTypeColor() {
            return typeColor;
        }

        @Override
        public String toString() {
            return "Unit{" +
                    "id='" + id + '\'' +
                    ", typeColor='" + typeColor + '\'' +
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
        public final static StationType REWORK = new StationType(5, "RW", null, StationOutputType.PASS, null, StationOutputType.WORK, null);
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

        public boolean isCommon() {
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

        public Unit getUnit(String key) {
            return units.get(key);
        }

        public void setUnit(String key, Unit unit) {
            units.put(key, unit);
        }

        public void removeUnit(String key) {
            units.remove(key);
        }
/*
        @Override
        public String toString() {
            return "WholeState{" +
                    "players=" + Arrays.toString(players) +
                    ", units=" + units +
                    '}';
        }
*/
        public void switchUnits(String unitKeyFrom, String unitKeyTo) {
            switchUnits.add(new String[]{unitKeyFrom, unitKeyTo});
        }

        public String hasSwitchUnit(Unit unit) {
            for(String[] switchUnit : switchUnits) {
                if(getUnit(switchUnit[0]) == unit)
                    return switchUnit[1];
            }
            return null;
        }

        public void makeSwitch() {
            for(String[] switchableUnits : switchUnits) {
                Unit from = getUnit(switchableUnits[0]);
                if(from == null) {
                    SystemSpecific.get().log("ERROR NOT SWITCH!!!!!!!!!!!!!!!!!!!!!");
                    continue;
                }
                if(getUnit(switchableUnits[1]) == null) {
                    setUnit(switchableUnits[1], from);
                    removeUnit(switchableUnits[0]);
                }
            }
            switchUnits.clear();
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

        public HashMap<String, Unit> getAllUnit() {
            return units;
        }
    }

    private final Map<Long, WholeState> timeStates = new HashMap<>();
    private WholeState currentState;
    private long time = 0;
    private long startTime = -1000;
    private long maxTime = 0;

    public boolean setNextState() {
        long closestBigger = -1;
        for (Map.Entry<Long, WholeState> entry : timeStates.entrySet()) {
            if (entry.getKey() > time && closestBigger < time) {
                closestBigger = entry.getKey();
            } else if (entry.getKey() > time && entry.getKey() < closestBigger) {
                closestBigger = entry.getKey();
            }
        }
        time = closestBigger;
        currentState = timeStates.get(time);
        return closestBigger != -1;
    }

    public WholeState getState() {
        if(currentState == null) {
            long closestBigger = -1;
            for (Map.Entry<Long, WholeState> entry : timeStates.entrySet()) {
                if (entry.getKey() >= time && closestBigger < time) {
                    closestBigger = entry.getKey();
                } else if (entry.getKey() >= time && entry.getKey() < closestBigger) {
                    closestBigger = entry.getKey();
                }
            }
            time = closestBigger;
            return currentState = timeStates.get(closestBigger);
        } else {
            return currentState;
        }
    }

    public long getTime() {
        return time;
    }

    public double getTimePercent() {
        return (time - startTime) / (double) (maxTime - startTime);
    }

    public void setTimePercent(double percent) {
        currentState = null;
        time = Math.round(percent * (maxTime - startTime) + startTime);
    }

    public RobotStates(String s) {
        String[] lines = s.split("\n");

        WholeState state = new WholeState();
        timeStates.put(0L, state);

        HashMap<String, String> incomings = new HashMap<>(); // Robot_id,Message_id -> time,command,oth

        HashSet<String> stationReady = new HashSet<>();
        for(int l = 0; l < lines.length; ++l) {
            String line = lines[l];
            if((line = line.trim()).isEmpty()) {
                continue;
            }
            if(l > 0 && line.equals(lines[l-1])) {
                continue;
            }

            if(line.indexOf("|") == -1 ||
                    line.indexOf("|", line.indexOf("|") + 1) == -1) {
                SystemSpecific.get().log("WARNING " + l + "  - MALFORMED LOG - not enough separator " + line);



                continue;

            }
            String[] infos = new String[] {
                    line.substring(0, line.indexOf("|")),
                    line.substring(line.indexOf("|") + 1, line.indexOf("|", line.indexOf("|") + 1)),
                    line.substring(line.indexOf("|", line.indexOf("|") + 1) + 1, line.lastIndexOf("|")),
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

            if(timestamp < maxTime) {
                SystemSpecific.get().log("WARNING " + l + "  - MALFORMED LOG - Back to time - " + timestamp + " " + maxTime + " aka " + infos[0]);
            }

            String player = infos[1];
            String incomeMessage = infos[2];
            String message = infos[3];

            boolean incoming = incomeMessage.contains("INCOMING");

            message = message.substring(1, message.length() - 1);

            if(message.charAt(0) == '<') {
                if(message.length() < 10) {
                    SystemSpecific.get().log("WARNING " + l + "  - MALFORMED LOG - message length < 10 " + message + " --- " + line);
                    continue;

                }
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
                    SystemSpecific.get().log("WARNING " + l + "  - MALFORMED LOG - " + infos[0] + " MORE THAN 1 KEY AT THE SAME TIME OF: " + key + " IGNORING --- " + timestamp + "," + message + " --- " + prev);
                } else {
                    incomings.put(key, value);
                }
            } else {
                if(messagePieces.length < 2) {
                    SystemSpecific.get().log("WARNING " + l + "  - MALFORMED LOG - NOT enough message pieces " + line);
                    continue;
                }

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
                            SystemSpecific.get().log("WARNING " + l + "  - MALFORMED LOG - " + infos[0] + " NO KEY FOR THIS MESSAGE: " + timestamp + "," + message + " IGNORING");
                            continue;
                        }
                        break;
                }
                boolean wasNew = false;
                switch (messagePieces[0]) {
                    case "STATION_READY":
                        if(stationReady.contains(message)) {
                            continue;
                        }
                        stationReady.add(message);
                    case "MOVE":
                    case "PICK":
                    case "PLACE":
                    case "UNITS_ON_CONVEYOR":
                    case "UNITS_ON_STATION":
                    case "QTY_INFO":
                        maxTime = Math.max(maxTime, (int) timestamp);

                        state = state.clone();
                        wasNew = true;

                        for(RobotPlayer robotPlayer : state.getPlayers()) {
                            String from = robotPlayer.getNextUnitFrom();
                            Unit unit = robotPlayer.getNextUnit();

                            robotPlayer.setFrom(robotPlayer.getTo());

                            if(from == null || unit == robotPlayer.getOwnedUnit())
                                continue;

                            if(unit == null) {
                                state.setUnit(from, robotPlayer.getOwnedUnit());
                                robotPlayer.setPrevPutted(true);
                                if(robotPlayer.getOwnedUnit() == null) {
                                    SystemSpecific.get().log("WARNING " + l + "  - unit is empty on get!!!!");
                                } else {
                                    state.addChangedTo(robotPlayer.getOwnedUnit());
                                }
                                robotPlayer.setTo(new Point(robotPlayer.getFrom().getX(), -1));
                            } else {
                                state.removeUnit(from);
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
                                    for(StationType stationType : robotPlayer.getStations()) {
                                        if(stationType != null && !stationType.isCommon()) {
                                            for(StationOutputType outType : stationType.getOutputs()) {
                                                if(outType == null)
                                                    continue;
                                                String unitKeyTo = stationType.getString(player) + "-" + outType.getAbbr();
                                                state.removeUnit(unitKeyTo);
                                            }
                                        }
                                    }
                                    robotPlayer.setScores("PASS: 0,0;FAIL: 0,0");
                                    robotPlayer.setOwnedUnit(null);
                                    robotPlayer.setNextUnit(null, null);
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
                                            Unit unit = state.getUnit(unitMapKey);

                                            //state.getUnits().remove(unitMapKey);
                                            if(unit == null) {
                                                SystemSpecific.get().log("WARNING " + l + "  - unit is empty on pick!!!!" + prev + " " + pos + " " + unitMapKey + " && " + line + " possible before WARNING " + l + " s");

                                                if(stationType != StationType.CONVEYOR_IN) {
                                                    SystemSpecific.get().log("ERROR " + l + " not conveyor, cannot what to do this time");
                                                    break;
                                                }
                                                unit = new Unit();
                                                state.setUnit(unitMapKey, unit);

                                                for(Map.Entry<Long, WholeState> stateEntry : timeStates.entrySet()) {
                                                    StationType stationTypeX = StationType.CONVEYOR_IN;
                                                    StationOutputType stationOutputTypeX = stationTypeX.getOutputs().get(
                                                            Integer.parseInt(pos.substring(4)));
                                                    String unitMapKeyX = stationTypeX.getString("") + "-" + stationOutputTypeX.getAbbr();

                                                    if(null == stateEntry.getValue().getUnit(unitMapKeyX)) {
                                                        stateEntry.getValue().setUnit(unitMapKeyX, unit);
                                                    }
                                                }
                                            }
                                            robotPlayer.setNextUnit(unit, unitMapKey);
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
                                            //state.setUnit(unitMapKey, robotPlayer.getOwnedUnit());
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
                                        Unit beforeUnit = state.getUnit(unitMapKey);
                                        if(!tokens[j * 2 + 3].trim().isEmpty()) {
                                            String[] unitStrings = tokens[j * 2 + 3].split(";");
                                            if(beforeUnit == null || !beforeUnit.getId().equals(unitStrings[0])) {
                                                state.setUnit(unitMapKey, new Unit(unitStrings[0], unitStrings.length > 1 ? unitStrings[1] : "-"));
                                            }
                                        }
                                    }
                                    break;
                                }
                            }

                            for(Map.Entry<Long, WholeState> stateEntry : timeStates.entrySet()) {
                                StationType stationType = StationType.CONVEYOR_IN;
                                for(int j = 0; j < 5; ++j) {
                                    StationOutputType stationOutputType = stationType.getOutputs().get(j);
                                    String unitMapKey = stationType.getString("") + "-" + stationOutputType.getAbbr();
                                    Unit nextUnit = state.getUnit(unitMapKey);
                                    if(stateEntry.getValue().getUnit(unitMapKey) == null && nextUnit != null) {
                                        stateEntry.getValue().setUnit(unitMapKey, nextUnit);
                                    }
                                }
                            }
                            break;
                        case "UNITS_ON_STATION":
                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (player.equals(robotPlayer.getName())) {
                                    robotPlayer.setFrom(robotPlayer.getTo());

                                    StationType stationType = robotPlayer.getStations().get((int) robotPlayer.getFrom().getX());
                                    String[] tokens = messagePieces[1].split("\"");

                                    if(tokens.length != 9) {
                                        SystemSpecific.get().log("ERROR " + l + " - not enough token for units on station in line --- " + line);
                                        break;
                                    }
                                    for(int j = 1; j < 4; ++j) {
                                        String stationOutput = tokens[j * 2];
                                        stationOutput = stationOutput.substring(stationOutput.indexOf('_') + 1, stationOutput.indexOf('='));
                                        String value = tokens[j * 2 + 1].trim();

                                        if(value.isEmpty()) {
                                            continue;
                                        }

                                        for(StationOutputType stationOutputType : stationType.getOutputs()) {
                                            if(stationOutputType != null &&
                                                    stationOutputType.getAbbr().equals(stationOutput.toUpperCase())) {
                                                String unitMapKey = stationType.getString(robotPlayer.getName()) + "-" +
                                                        stationOutputType.getAbbr();
                                                Unit beforeUnit = state.getUnit(unitMapKey);

                                                if(beforeUnit == null) {
                                                    SystemSpecific.get().log("WARNING " + l + "  - NOT KNOWN UNITS ON STATION --- " + line + " --- ");
                                                    String unitPlace = "";
                                                    for(Map.Entry<String, Unit> entry : state.getAllUnit().entrySet()) {
                                                        if(entry.getValue().getId().equals(value)) {
                                                            unitPlace = entry.getKey();
                                                            break;
                                                        }
                                                    }
                                                    if(unitPlace.isEmpty()) {
                                                        SystemSpecific.get().log("WARNING CANNOT FIND ELEMENT - lets create it");
                                                        state.setUnit(unitMapKey, new Unit().setId(value));
                                                    } else {
                                                        SystemSpecific.get().log("WARNING UNIT IS HERE - " + unitPlace + " NO wait for the READY message");
                                                        state.setUnit(unitMapKey, state.getUnit(unitPlace));
                                                        state.removeUnit(unitPlace);
                                                    }
                                                } else if(beforeUnit.getId().isEmpty()) {
                                                    beforeUnit.setId(value);
                                                } else if(!beforeUnit.getId().equals(value)) {
                                                    SystemSpecific.get().log("ERROR " + l + " - NOT THE SAME UNIT ON STATION!!! - " + line + " AND " + beforeUnit);
                                                    //beforeUnit.setId(value);
                                                }

                                                break;
                                            }
                                        }
                                    }

                                    break;
                                }
                            }
                            // ...
                            break;
                        case "STATION_READY":
                            String result = messagePieces[3].toUpperCase();
                            String station = messagePieces[1];
                            String id = messagePieces[2];

                            for(int i = 0; i < state.getPlayers().length; ++i) {
                                RobotPlayer robotPlayer = state.getPlayers()[i];
                                if (robotPlayer.getName().equals(player)) {
                                    for(StationType stationType : robotPlayer.getStations()) {

                                        if(stationType != null && stationType.getAbbr().equals(station)) {
                                            String unitKeyFrom = stationType.getString(player) + "-WORK";
                                            String unitKeyTo = stationType.getString(player) + "-" + result;

                                            Unit fromUnit = state.getUnit(unitKeyFrom);

                                            if(fromUnit == null) {
                                                SystemSpecific.get().log("WARNING " + l + "  - Nothing on WORK station is ready :( ");

                                            } else if(fromUnit.getId().isEmpty()) {
                                                fromUnit.setId(id);
                                            } else if(!fromUnit.getId().equals(id)) {
                                                SystemSpecific.get().log("WARNING " + l + "  - NOT THE SAME UNIT AS WORK STATION!!!");
                                                break;
                                            }

                                            state.switchUnits(unitKeyFrom, unitKeyTo);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        case "QTY_INFO": // DIREKT NINCS BREAK
                        case "END_GAME":
                            String good;
                            String bad = "";
                            if(messagePieces[1].startsWith("PASS")) {
                                good = messagePieces[1].substring(messagePieces[1].indexOf("=") + 1) + ",0";
                                bad = messagePieces[2].substring(messagePieces[2].indexOf("=") + 1) + ",0";
                            } else {
                                good = messagePieces[1].substring(messagePieces[1].lastIndexOf("=") + 1);
                                for(int i = 2; i < messagePieces.length - 1; ++i) {
                                    if(messagePieces[i].indexOf("=") == -1 ||
                                            messagePieces[i].indexOf("<") == -1) {
                                        SystemSpecific.get().log("WARNING " + l + "  - MALFORMED LOG - messagePieces not contains = or < " + messagePieces[i]);
                                        continue;
                                    }

                                    good += "," + messagePieces[i].substring(messagePieces[i].lastIndexOf("=") + 1);
                                    bad += messagePieces[i].substring(
                                            messagePieces[i].indexOf("=") + 1,
                                            messagePieces[i].indexOf("<")) + ",";

                                }
                                bad += messagePieces[messagePieces.length - 1].substring(
                                        messagePieces[messagePieces.length - 1].indexOf("=") + 1,
                                        messagePieces[messagePieces.length - 1].indexOf("<"));
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
                    while (timeStates.containsKey(timestamp)) {
                        ++timestamp;
                    }
                    timeStates.put(timestamp, state);
                }
            }

        }

        RobotPlayer player1 = state.getPlayers()[0];
        String[] player1Score = player1.getScores().split(";");

        if (player1Score[0].substring(player1Score[0].indexOf(" ") + 1, player1Score[0].indexOf(",")).charAt(0) == '=') {
            SystemSpecific.get().log("Final score contains = " + player1Score[0].substring(player1Score[0].indexOf(" ") + 1, player1Score[0].indexOf(",")));
            return;
        }


        int player1FinalScore =  Integer.parseInt(player1Score[0].substring(player1Score[0].indexOf(" ") + 1, player1Score[0].indexOf(","))) +
                Integer.parseInt(player1Score[1].substring(player1Score[1].indexOf(" ") + 1, player1Score[1].indexOf(",")));

        RobotPlayer player2 = state.getPlayers()[1];
        String[] player2Score = player2.getScores().split(";");

        if (player2Score[0].substring(player2Score[0].indexOf(" ") + 1, player2Score[0].indexOf(",")).charAt(0) == '=') {
            SystemSpecific.get().log("Final score contains = " + player2Score[0].substring(player2Score[0].indexOf(" ") + 1, player2Score[0].indexOf(",")));
            return;
        }

        int player2FinalScore =  Integer.parseInt(player2Score[0].substring(player2Score[0].indexOf(" ") + 1, player2Score[0].indexOf(","))) +
                Integer.parseInt(player2Score[1].substring(player2Score[1].indexOf(" ") + 1, player2Score[1].indexOf(",")));

        int winnerIndex = player1FinalScore > player2FinalScore ? 0 : 1;

        state = state.clone();

        state.getPlayers()[winnerIndex].setTo(new Point(winnerIndex == 0 ? 0 : 3, -1));
        timeStates.put(maxTime = maxTime + (maxTime - startTime) / timeStates.size(), state);

        state = state.clone();

        state.getPlayers()[winnerIndex].setFrom(state.getPlayers()[winnerIndex].getTo());
        state.getPlayers()[winnerIndex].setTo(new Point(winnerIndex == 0 ? 7 : 4, -1));
        timeStates.put(maxTime = maxTime + (maxTime - startTime) / timeStates.size(), state);

    }
}
