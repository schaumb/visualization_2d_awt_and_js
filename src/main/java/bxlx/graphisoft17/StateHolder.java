package bxlx.graphisoft17;

import bxlx.graphics.Direction;
import bxlx.graphics.Point;
import bxlx.graphisoft17.element.Display;
import bxlx.graphisoft17.element.Field;
import bxlx.graphisoft17.element.Player;
import bxlx.graphisoft17.element.Princess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.IntFunction;

/**
 * Created by ecosim on 4/27/17.
 */
public class StateHolder {
    private int stateIndex;
    private String[] states;
    private List<Display> displays = new ArrayList<>();
    private List<Princess> princesses = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<List<Field>> fields = new ArrayList<>();
    private List<Point> blocked = new ArrayList<>();
    private HashMap<String, int[]> points = new HashMap<>();
    private boolean isTest;
    private int maxTick;
    private int level;

    private List<Field> moveFields = new ArrayList<>();
    private Point moveDirection;

    private List<Field> affectedRoutes = new ArrayList<>();


    public StateHolder(String everything) {
        this.states = everything.split("PLAYERTURN ");

        String[] preMessages = states[0].split("\nID ");
        String[] allPoint = preMessages[0].split("\n");

        for(String point : allPoint) {
            String[] parameters = point.split(" ");
            if(!parameters[0].equals("#")) {
                points.put(parameters[0], new int[] {
                        Integer.parseInt(parameters[1]),
                        Integer.parseInt(parameters[2])
                });
            }
        }

        String[] messages = preMessages[1].split("\n");

        for(String message : messages) {
            String[] parameters = message.split(" ");

            switch (parameters[0]) {
                case "PLAYERS":
                    for(int i = 1; i < parameters.length; ++i) {
                        princesses.add(new Princess(i - 1));
                        players.add(new Player(i - 1, parameters[i]));
                    }
                    break;
                case "LEVEL":
                    this.level = Integer.parseInt(parameters[1]);
                    break;
                case "SIZE":
                    int n = Integer.parseInt(parameters[1]);
                    int m = Integer.parseInt(parameters[2]);
                    for(int j = 0; j < m; ++j) {
                        List<Field> line = new ArrayList<>();
                        for(int i = 0; i < n; ++i) {
                            line.add(null);
                        }
                        fields.add(line);
                    }
                    break;
                case "DISPLAYS":
                    int d = Integer.parseInt(parameters[1]);
                    for(int i = 0; i < d; ++i) {
                        displays.add(new Display());
                    }
                    break;
                case "BLOCKED":
                    int x = Integer.parseInt(parameters[1]);
                    int y = Integer.parseInt(parameters[2]);
                    blocked.add(new Point(x, y));
                    break;
                case "TEST":
                    int t = Integer.parseInt(parameters[1]);
                    isTest = t == 1;
                    break;
                case "MAXTICK":
                    maxTick = Integer.parseInt(parameters[1]);
                    break;
                case "TARGETS":
                    int player = Integer.parseInt(parameters[1]);
                    List<Display> playerDisplays = new ArrayList<>();
                    for(int i = 2; i < parameters.length; ++i) {
                        playerDisplays.add(displays.get(
                                Integer.parseInt(parameters[i])
                        ));
                    }

                    players.get(player).setDisplays(playerDisplays);
                    break;
            }
        }
        setState(1);
    }

    public synchronized void setState(int stateIndex) {
        this.stateIndex = stateIndex;
        String state = states[stateIndex];

        for(Display display : displays) {
            display.deactivate();
        }

        moveFields.clear();
        moveDirection = null;
        affectedRoutes.clear();

        players.get(getWhosTurn()).setZeroCommands();

        String[] playerInfo = state.split("PLAYER ");
        String[] messages = playerInfo[0].split("\n");

        for(String message : messages) {
            String[] parameters = message.split(" ");
            switch (parameters[0]) {
                case "FIELDS": {
                    int index = 1;
                    for (int j = 0; j < fields.size(); ++j) {
                        List<Field> line = fields.get(j);
                        for (int i = 0; i < line.size(); ++i, ++index) {
                            Field f = line.get(i);

                            int type = Integer.parseInt(parameters[index]);
                            Point point = new Point(i, j);

                            if(f != null && f.getType() != type) {
                                //SystemSpecific.get().log("NOT EQUAL TYPES");
                                f = null;
                            }

                            if(f == null) {
                                f = new Field(type);
                                line.set(i, f);
                                f.setPosition(point);
                            }
                            if(!f.getPosition().equals(point)) {
                                //SystemSpecific.get().log("NOT EQUAL POSITION");
                                f.setPosition(point);
                            }
                            f.clearPrincesses();
                        }
                    }
                    break;
                }
                case "DISPLAY": {
                    int index = Integer.parseInt(parameters[1]);
                    int x = Integer.parseInt(parameters[2]);
                    int y = Integer.parseInt(parameters[3]);
                    Field field = fields.get(y).get(x);
                    Display fieldDisplay = field.getDisplay();
                    Display setToDisplay = displays.get(index);

                    if(fieldDisplay != null && fieldDisplay != setToDisplay) {
                        //SystemSpecific.get().log("NOT EQUAL DISPLAY ON FIELD");
                        fieldDisplay = null;
                    }

                    if(fieldDisplay == null) {
                        field.setDisplay(setToDisplay);
                    }

                    setToDisplay.activate();
                    break;
                }
                case "POSITION": {
                    int index = Integer.parseInt(parameters[1]);
                    int x = Integer.parseInt(parameters[2]);
                    int y = Integer.parseInt(parameters[3]);
                    Field field = fields.get(y).get(x);
                    Princess princess = princesses.get(index);

                    Point point = new Point(x, y);
                    Point princessPosition = princess.getPosition();

                    if(princessPosition != null && !princessPosition.equals(point)) {
                        //SystemSpecific.get().log("NOT EQUAL PRINCESS POSITION");
                    }

                    field.setPrincess(princess);
                    break;
                }
            }
        }
        for(int i = 1; i < playerInfo.length; ++i) {
            String[] lines = playerInfo[i].split("\n");

            Princess princess = princesses.get(i - 1);
            Player player = players.get(i - 1);

            for(String line : lines) {
                String[] parameters = line.split(" ");
                switch (parameters[0]) {
                    case "MESSAGE":
                        player.setMessage(line.substring(8));
                        break;
                    case "TARGET":
                        int target = Integer.parseInt(parameters[1]);

                        displays.get(target).setPrincess(princess);
                        break;
                    case "EXTRAFIELD":
                        int extra = Integer.parseInt(parameters[1]);
                        player.setExtra(new Field(extra));
                        break;
                    case "GAMESCORE":
                        int score = Integer.parseInt(parameters[1]);
                        if(player.getScore() != score) {
                            //SystemSpecific.get().log("NOT EQUAL GAMESCORE");
                            player.setScore(score);
                        }
                        break;
                    case "PUSH":
                        boolean column = Integer.parseInt(parameters[1]) == 1;
                        boolean positive = Integer.parseInt(parameters[2]) == 1;
                        int nTh = Integer.parseInt(parameters[3]);
                        int fieldType = Integer.parseInt(parameters[4]);

                        int maxMoving = column ? fields.size() : fields.get(nTh).size();
                        int from = !positive ? 0 : maxMoving - 1;
                        int to = !positive ? maxMoving - 1 : 0;
                        int by = !positive ? 1 : -1;
                        IntFunction<Field> getField = j -> column ? fields.get(j).get(nTh) : fields.get(nTh).get(j);

                        moveDirection = new Point(column ? 0 : -by, column ? -by : 0);

                        Field fromField = getField.apply(from);
                        moveFields.add(fromField);

                        for (int j = to; j != from; j -= by) {
                            moveFields.add(getField.apply(j));
                        }

                        Field f = new Field(fieldType);
                        f.setPosition(getField.apply(to).getPosition().add(moveDirection.multiple(-1)));
                        moveFields.add(f);

                        fromField.moveElements(f);

                        players.get(getWhosTurn()).setPushMessage(
                                new Player.PushMessage(column, positive, nTh, fieldType));
                        break;
                    case "GOTO":
                        int x = Integer.parseInt(parameters[1]);
                        int y = Integer.parseInt(parameters[2]);

                        Point movePrincessTo = new Point(x, y);

                        players.get(getWhosTurn()).setGotoMessage(movePrincessTo);
                        break;
                }
            }
        }
    }

    private void calculateAffectedRoutes(Point from, Point to) {
        affectedRoutes.clear();
        if(from.equals(to))
            return;

        HashMap<Point, Point> reachable = new HashMap<>();
        Queue<Point> visitQueue = new LinkedList<>();
        visitQueue.add(from);
        reachable.put(from, from);

        while (!visitQueue.isEmpty()) {
            Point visit = visitQueue.peek();
            visitQueue.remove(visit);
            for (Direction direction : Direction.values()) {
                Point next = new Point(visit.getX() + direction.getVector().getX(),
                        visit.getY() + direction.getVector().getY());
                if (!validCoordinate(next))
                    continue;

                boolean was = false;

                for(HashMap.Entry<Point, Point> entry : reachable.entrySet()) {
                    if(entry.getKey().equals(next)) {
                        was = true;
                        break;
                    }
                }

                if (!was &&
                        Field.hasRouteToStatic(getField(visit),
                                getField(next), direction)) {

                    visitQueue.add(next);
                    reachable.put(next, visit);
                    if (next.equals(to)) {
                        break;
                    }
                }
            }
        }
        Point backward = to;
        while(!backward.equals(from)) {
            affectedRoutes.add(getField(backward));

            boolean was = false;

            for(HashMap.Entry<Point, Point> entry : reachable.entrySet()) {
                if(entry.getKey().equals(backward)) {
                    was = true;
                    backward = entry.getValue();
                    break;
                }
            }

            if(!was)
                break;
        }
        affectedRoutes.add(getField(from));
    }

    private boolean validCoordinate(Point newPoint) {
        return 0 <= newPoint.getY() && newPoint.getY() < fields.size()
                && 0 <= newPoint.getX() && newPoint.getX() < fields.get(0).size();
    }

    private Field getField(Point position) {
        return fields.get((int) position.getY())
                .get((int) position.getX());
    }

    private void setField(Point position, Field field) {
        fields.get((int) position.getY())
                .set((int) position.getX(), field);
    }

    private int getTick() {
        return (stateIndex - 1) / princesses.size();
    }

    public int getWhosTurn() {
        return (stateIndex - 1) % princesses.size();
    }

    public Player getPlayerWhosTurn() {
        return players.get(getWhosTurn());
    }

/*
    protected synchronized void forceRedraw(ICanvas canvas) {
        Timer timer = new Timer(200);

        if(fields.size() == 0) {
            return;
        }

        Rectangle boundRect = canvas.getBoundingRectangle();
        double elemSize = boundRect.getSize().asPoint().multiple(new Point(fields.get(0).size(), fields.size()).inverse())
            .asSize().getShorterDimension();

        canvas.clip(new Rectangle(boundRect.getStart(), new Point(fields.get(0).size(), fields.size()).multiple(elemSize).asSize()));

        Function<Point, Rectangle> clipping = p -> new Rectangle(boundRect.getStart()
                .add(p.multiple(elemSize)),
                Size.square(elemSize));
        UnaryOperator<Rectangle> fake = r -> r.withStart(r.getStart().add(r.getSize().asPoint().multiple(-1.0 / 6).add(-1)))
                .withSize(r.getSize().asPoint().multiple(1 + 2.0 / 6).add(2).asSize());

        Consumer<Field> forceDrawField = field -> {
            Rectangle clip = clipping.applyer(field.getPosition());

            canvas.clip(clip);
            canvas.fakeClip(fake.applyer(clip));

            field.forceDraw(canvas);

            canvas.fakeRestore();
            canvas.restore();
        };

        switch (state) {
            case FIRST_DRAW: {
                timer.setStart();
                for (List<Field> line : fields) {
                    for (Field field : line) {
                        if (field == null || field.getPosition() == null) {
                            continue;
                        }
                        forceDrawField.accept(field);
                    }
                }

                state = States.MOVE_ELEMENTS;
                break;
            }
            case MOVE_ELEMENTS: {
                if(moveFields.isEmpty()) {
                    setNextState();
                    timer.setStart();
                    break;
                }
                for (Field field : moveFields) {
                    Point moveAdd = moveDirection.multiple(elemSize * Math.min(1, timer.percent() * 2));

                    Rectangle clip = clipping.applyer(field.getPosition());
                    clip = clip.withStart(clip.getStart().add(moveAdd));

                    canvas.clip(clip);
                    canvas.fakeClip(fake.applyer(clip));

                    field.forceDraw(canvas);

                    canvas.fakeRestore();
                    canvas.restore();
                }
                if(timer.percent() >= 0.5) {
                    state = States.MOVE_PRINCESS;
                    commitMovements();
                }
                break;
            }
            case MOVE_PRINCESS: {
                Princess princess = princesses.get(getWhosTurn());
                Point pp = princess.getPosition();
                Point ptp = princess.getToPosition();

                if(ptp == null || pp.equals(ptp)) {
                    setNextState();
                    timer.setStart();
                    state = States.MOVE_ELEMENTS;
                    break;
                }

                forceDrawField.accept(fields.get((int) pp.getY()).get((int) pp.getX()));
                forceDrawField.accept(fields.get((int) ptp.getY()).get((int) ptp.getX()));

                Point moveAdd = new Point(0.5, 0).multiple(elemSize * Math.max(0, timer.percent() * 2 - 1));

                Rectangle clip = clipping.applyer(pp);
                Rectangle clip2 = clip.withStart(clip.getStart().add(moveAdd));

                canvas.clip(clip2);

                canvas.clip(clip
                        .withStart(clip.getStart().add(
                                new Point(clip.getSize().getWidth() / 3.0,0)))
                        .withSize(clip.getSize().asPoint().multiple(new Point(1 / 3.0, 1)).asSize()));

                canvas.fakeClip(fake.applyer(clip2));

                princess.forceDraw(canvas);

                canvas.fakeRestore();

                canvas.restore();
                canvas.restore();


                clip = clipping.applyer(ptp.add(new Point(-0.5, 0)));
                clip = clip.withStart(clip.getStart().add(moveAdd));

                canvas.clip(clip);

                Rectangle ptpClip = clipping.applyer(ptp);

                canvas.clip(ptpClip
                        .withStart(ptpClip.getStart().add(
                                new Point(ptpClip.getSize().getWidth() / 3.0,0)))
                        .withSize(ptpClip.getSize().asPoint().multiple(new Point(1 / 3.0, 1)).asSize()));

                canvas.fakeClip(fake.applyer(clip));

                princess.forceDraw(canvas);

                canvas.fakeRestore();
                canvas.restore();
                canvas.restore();

                if(timer.elapsed()) {
                    state = States.MOVE_ELEMENTS;
                    timer.setStart();
                    commitPrincessMove();
                    setNextState();
                }
                break;
            }
        }
        canvas.restore();
    }
*/

    public int getStateIndex() {
        return stateIndex;
    }

    public boolean hasNextState() {
        return states.length > stateIndex + 1;
    }

    public void nextState() {
        int state = stateIndex + 1;

        if(states.length > state) {
            setState(state);
        }
    }

    public void finalizePush() {
        for(Field field : moveFields) {
            field.setPosition(field.getPosition().add(moveDirection));

            Point pos = field.getPosition();

            if(validCoordinate(pos))
                setField(pos, field);
        }
        moveFields.clear();
        moveDirection = null;

        Player player = getPlayerWhosTurn();
        Princess princess = princesses.get(getWhosTurn());
        player.commitPushMessage();

        if(player.getGotoMessage() != null) {
            calculateAffectedRoutes(princess.getPosition(),
                    player.getGotoMessage());
        }
    }

    public void removePrincess() {
        Player player = getPlayerWhosTurn();
        Princess princess = princesses.get(getWhosTurn());
        if(player.getGotoMessage() != null && !player.getGotoMessage().equals(princess.getPosition())) {
            getField(princess.getPosition()).removePrincess(princess);
        }

    }

    public void finalizeGoto() {
        Player player = getPlayerWhosTurn();
        Princess princess = princesses.get(getWhosTurn());

        getField(player.getGotoMessage()).setPrincess(princess);

        affectedRoutes.clear();
    }

    public List<Display> getDisplays() {
        return displays;
    }

    public List<Princess> getPrincesses() {
        return princesses;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<List<Field>> getFields() {
        return fields;
    }

    public List<Point> getBlocked() {
        return blocked;
    }

    public int[] getPointTo(String someone) {
        int[] point = points.get(someone);
        if(point == null) {
            point = new int[] {0, 0};
            points.put(someone, point);
        }

        return point;
    }

    public boolean isTest() {
        return isTest;
    }

    public int getMaxTick() {
        return maxTick;
    }

    public int getLevel() {
        return level;
    }

    public List<Field> getMoveFields() {
        return moveFields;
    }

    public Point getMoveDirection() {
        return moveDirection;
    }

    public List<Field> getAffectedRoutes() {
        return affectedRoutes;
    }
}
