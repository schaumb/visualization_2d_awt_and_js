package bxlx.graphisoft;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.shapes.Rectangle;
import bxlx.graphisoft.element.Display;
import bxlx.graphisoft.element.Field;
import bxlx.graphisoft.element.Player;
import bxlx.graphisoft.element.Princess;
import bxlx.system.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

/**
 * Created by ecosim on 4/27/17.
 */
public class ElementHolder extends ChangeableDrawable {
    private final Timer timer = new Timer(4000);
    private final ChangeableValue<Integer> stateIndex;
    private final ChangeableValue<Double> time;
    private String[] states;
    private List<Display> displays = new ArrayList<>();
    private List<Princess> princesses = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<List<Field>> fields = new ArrayList<>();
    private int maxTick;
    private int tick;
    private int whosTurn;

    private List<Field> moveElements = new ArrayList<>();

    private Point moveDirection;

    public ElementHolder() {

        this.stateIndex = new ChangeableValue<>(this, 1);
        this.time = new ChangeableValue<>(this, () -> timer.percent());

        for(int i = 0; i < 4; ++i) {
            new DrawImage(Parameters.imgDir() + "q" + i + ".png");
            new DrawImage(Parameters.imgDir() + "b" + i + ".png");
        }
        for(int i = 1; i < 16; ++i) {
            new DrawImage(Parameters.imgDir() + i + ".jpg");
        }
        new DrawImage(Parameters.imgDir() + "m_on.jpg");
        new DrawImage(Parameters.imgDir() + "m_off.jpg");
    }


    public synchronized void createElements(String[] states) {

        removeElements();
        this.states = states;

        String[] messages = states[0].split("\n");

        for(String message : messages) {
            String[] parameters = message.split(" ");

            switch (parameters[0]) {
                case "PLAYERS":
                    for(int i = 1; i < parameters.length; ++i) {
                        princesses.add(new Princess(i - 1));
                        players.add(new Player(i - 1, parameters[i]));
                    }
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
                case "MAXTICK":
                    maxTick = Integer.parseInt(parameters[1]);
                    break;
            }
        }
        setState(1);
    }

    public synchronized void setState(Integer stateIndex) {
        String state = states[stateIndex];
        for(Display display : displays) {
            display.setStartState();
        }
        for(List<Field> line : fields) {
            for(Field field : line) {
                if(field != null) {
                    field.setNoMove();
                }
            }
        }
        for(Princess princess : princesses) {
            princess.setNoMove();
        }
        for(Player player : players) {
            player.setNoMove();
        }

        moveElements.clear();

        String[] playerInfo = state.split("PLAYER ");
        String[] messages = playerInfo[0].split("\n");

        whosTurn = Integer.parseInt(messages[0]);

        for(String message : messages) {
            String[] parameters = message.split(" ");
            switch (parameters[0]) {
                case "TICK":
                    tick = Integer.parseInt(parameters[1]);
                    break;
                case "FIELDS": {
                    int index = 1;
                    for (int j = 0; j < fields.size(); ++j) {
                        List<Field> line = fields.get(j);
                        for (int i = 0; i < line.size(); ++i) {
                            Field newField = new Field(Integer.parseInt(parameters[index]));
                            line.set(i, newField);
                            newField.setPosition(new Point(i, j));
                            ++index;
                        }
                    }
                    break;
                }
                case "DISPLAY": {
                    int index = Integer.parseInt(parameters[1]);
                    int x = Integer.parseInt(parameters[2]);
                    int y = Integer.parseInt(parameters[3]);
                    Display display = displays.get(index);
                    fields.get(y).get(x).setDisplay(display);
                    display.setActive();
                    break;
                }
                case "POSITION": {
                    int index = Integer.parseInt(parameters[1]);
                    int x = Integer.parseInt(parameters[2]);
                    int y = Integer.parseInt(parameters[3]);

                    Princess princess = princesses.get(index);
                    fields.get(y).get(x).setPrincess(princess);
                    princess.setPosition(new Point(x, y));
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
                        player.addExtra(extra);
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
                        for (int j = to; j != from; j -= by) {
                            getField.apply(j).setMove();
                            moveElements.add(getField.apply(j));
                        }
                        Field fromField = getField.apply(from);
                        fromField.setMove();
                        moveElements.add(fromField);

                        Field f = new Field(fieldType);
                        fromField.addElements(f);
                        f.setPosition(getField.apply(to).getPosition().add(moveDirection.multiple(-1)));
                        f.setMove();
                        moveElements.add(f);

                        player.setMove(getField.apply(from).getType(), fieldType);
                        break;
                    case "GOTO":
                        int x = Integer.parseInt(parameters[1]);
                        int y = Integer.parseInt(parameters[2]);

                        Princess p = princesses.get(whosTurn);
                        Point toMove = new Point(x, y);

                        p.setMove(toMove);
                        break;
                }
            }
        }
        this.state = State.FIRST_DRAW;
        this.stateIndex.setElem(stateIndex);
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().setIf(stateIndex.isChanged(), Redraw.PARENT_NEED_REDRAW)
                .setIf(this.state != State.END, Redraw.I_NEED_REDRAW);
    }

    public enum State {
        FIRST_DRAW,
        MOVE_ELEMENTS,
        MOVE_PRINCESS,
        END
    }

    private State state = State.FIRST_DRAW;

    @Override
    protected synchronized void forceRedraw(ICanvas canvas) {
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
            Rectangle clip = clipping.apply(field.getPosition());

            canvas.clip(clip);
            canvas.fakeClip(fake.apply(clip));

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

                state = State.MOVE_ELEMENTS;
                break;
            }
            case MOVE_ELEMENTS: {
                if(moveElements.isEmpty()) {
                    setNextState();
                    timer.setStart();
                    break;
                }
                for (Field field : moveElements) {
                    Point moveAdd = moveDirection.multiple(elemSize * Math.min(1, timer.percent() * 2));

                    Rectangle clip = clipping.apply(field.getPosition());
                    clip = clip.withStart(clip.getStart().add(moveAdd));

                    canvas.clip(clip);
                    canvas.fakeClip(fake.apply(clip));

                    field.forceDraw(canvas);

                    canvas.fakeRestore();
                    canvas.restore();
                }
                if(timer.percent() >= 0.5) {
                    state = State.MOVE_PRINCESS;
                    commitMovements();
                }
                break;
            }
            case MOVE_PRINCESS: {
                Princess princess = princesses.get(whosTurn);
                Point pp = princess.getPosition();
                Point ptp = princess.getToPosition();

                if(ptp == null || pp.equals(ptp)) {
                    setNextState();
                    timer.setStart();
                    state = State.MOVE_ELEMENTS;
                    break;
                }

                forceDrawField.accept(fields.get((int) pp.getY()).get((int) pp.getX()));
                forceDrawField.accept(fields.get((int) ptp.getY()).get((int) ptp.getX()));

                Point moveAdd = new Point(0.5, 0).multiple(elemSize * Math.max(0, timer.percent() * 2 - 1));

                Rectangle clip = clipping.apply(pp);
                Rectangle clip2 = clip.withStart(clip.getStart().add(moveAdd));

                canvas.clip(clip2);

                canvas.clip(clip
                        .withStart(clip.getStart().add(
                                new Point(clip.getSize().getWidth() / 3.0,0)))
                        .withSize(clip.getSize().asPoint().multiple(new Point(1 / 3.0, 1)).asSize()));

                canvas.fakeClip(fake.apply(clip2));

                princess.forceDraw(canvas);

                canvas.fakeRestore();

                canvas.restore();
                canvas.restore();


                clip = clipping.apply(ptp.add(new Point(-0.5, 0)));
                clip = clip.withStart(clip.getStart().add(moveAdd));

                canvas.clip(clip);

                Rectangle ptpClip = clipping.apply(ptp);

                canvas.clip(ptpClip
                        .withStart(ptpClip.getStart().add(
                                new Point(ptpClip.getSize().getWidth() / 3.0,0)))
                        .withSize(ptpClip.getSize().asPoint().multiple(new Point(1 / 3.0, 1)).asSize()));

                canvas.fakeClip(fake.apply(clip));

                princess.forceDraw(canvas);

                canvas.fakeRestore();
                canvas.restore();
                canvas.restore();

                if(timer.elapsed()) {
                    state = State.MOVE_ELEMENTS;
                    timer.setStart();
                    commitPrincessMove();
                    setNextState();
                }
                break;
            }
        }
        canvas.restore();
    }

    private void setNextState() {
        int state = stateIndex.get() + 1;

        if(states.length > state) {
            setState(state);
        } else {
            this.state = State.END;
            timer.setPercent(100);
        }
    }

    private void commitPrincessMove() {
        Princess p = princesses.get(whosTurn);
        Point to;
        p.setPosition(to = p.getToPosition());

        fields.get((int) to.getY()).get((int) to.getX()).setPrincess(p);

        p.setNoMove();
    }

    private void commitMovements() {
        for(Field field : moveElements) {
            field.setPosition(field.getPosition().add(moveDirection));

            Point pos = field.getPosition();

            if(0 <= pos.getY() && pos.getY() < fields.size()) {
                List<Field> line = fields.get((int) pos.getY());
                if(0 <= pos.getX() && pos.getX() < line.size()) {
                    line.set((int) pos.getX(), field);
                }
            }
            field.setNoMove();
        }
        moveElements.clear();

        Princess p = princesses.get(whosTurn);
        if(p.getToPosition() != null) {
            Point pos = p.getPosition();
            fields.get((int) pos.getY()).get((int) pos.getX()).removePrincess(p);
        }

        players.get(whosTurn).setNoMove();
    }

    public synchronized void removeElements() {
        displays.clear();
        princesses.clear();
        players.clear();
        fields.clear();
        moveElements.clear();
    }
}
