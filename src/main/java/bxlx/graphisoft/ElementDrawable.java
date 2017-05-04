package bxlx.graphisoft;

import bxlx.graphics.*;
import bxlx.graphics.container.Container;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.shapes.Rectangle;
import bxlx.graphisoft.element.Display;
import bxlx.graphisoft.element.Field;
import bxlx.system.SystemSpecific;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by ecosim on 2017.05.04..
 */
public class ElementDrawable extends ChangeableDrawable {
    private final StateHolder stateHolder;
    private final PlayState playState;
    private final ChangeableValue<PlayState.States> statesChangeableValue;
    private final ChangeableValue<Double> statePercent;
    private final ChangeableValue<Integer> stateIndex;

    public ElementDrawable(StateHolder stateHolder, PlayState playState) {
        this.stateHolder = stateHolder;
        this.playState = playState;

        statesChangeableValue = new ChangeableValue<>(this, () -> playState.getState());
        statePercent = new ChangeableValue<>(this, () -> playState.getState().getTimer().percent());
        stateIndex = new ChangeableValue<>(this, () -> stateHolder.getStateIndex());
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        PlayState.States currentState = statesChangeableValue.get();
        Double percent = statePercent.get();
        //SystemSpecific.get().log("Current status is: " + currentState + " and percent: " + percent);

        List<List<Field>> fields = stateHolder.getFields();
        if(fields.size() == 0) {
            return;
        }

        Rectangle boundRect = canvas.getBoundingRectangle();
        double elemSize = boundRect.getSize().asPoint().multiple(new Point(
                fields.get(0).size(), fields.size()).inverse())
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

            field.drawable().forceDraw(canvas);

            canvas.fakeRestore();
            canvas.restore();
        };

        if(currentState == PlayState.States.BEFORE_PUSH || needRedraw().iNeedRedraw()) {
            for (List<Field> line : fields) {
                for (Field field : line) {
                    forceDrawField.accept(field);
                }
            }
        }
        switch (currentState) {
            case PUSH:
                for (Field field : stateHolder.getMoveFields()) {
                    Point moveAdd = stateHolder.getMoveDirection().multiple(elemSize * Math.min(1, percent));

                    Rectangle clip = clipping.apply(field.getPosition());
                    clip = clip.withStart(clip.getStart().add(moveAdd));

                    canvas.clip(clip);
                    canvas.fakeClip(fake.apply(clip));

                    field.drawable().forceDraw(canvas);

                    canvas.fakeRestore();
                    canvas.restore();
                }
                break;
            case GOTO:
                int princessIndex = stateHolder.getWhosTurn();
                List<Field> affectedRoutes = stateHolder.getAffectedRoutes();

                int sizeOfAffectedRoutes = affectedRoutes.size();

                int res = (int) (percent * (sizeOfAffectedRoutes + 1));

                if(res == sizeOfAffectedRoutes + 1) {
                    --res;
                }

                if(res != 0) {
                    Point position = affectedRoutes.get(sizeOfAffectedRoutes - 1 - (res - 1)).getPosition();
                    Rectangle clip = clipping.apply(position);

                    canvas.clip(clip);
                    canvas.fakeClip(fake.apply(clip));

                    Parameters.getPrincessMoveField(princessIndex).forceDraw(canvas);

                    canvas.fakeRestore();
                    canvas.restore();
                }

                if(res != sizeOfAffectedRoutes) {
                    Point position = affectedRoutes.get(sizeOfAffectedRoutes - 1 - res).getPosition();
                    Rectangle clip = clipping.apply(position);

                    canvas.clip(clip);
                    canvas.fakeClip(fake.apply(clip));

                    Parameters.getPrincessMoveField(princessIndex).forceDraw(canvas);

                    canvas.fakeRestore();
                    canvas.restore();
                }
                for(int i = res - 2; i >= 0; --i) {
                    forceDrawField.accept(affectedRoutes.get(sizeOfAffectedRoutes - 1 - i));
                }

                Display display = stateHolder.getPlayerWhosTurn().getTargetDisplay();

                Point getDisplayPosition = display.getPosition();

                Rectangle clip = clipping.apply(getDisplayPosition);

                canvas.clip(clip);

                canvas.clip(clip.getScaled(0.7));

                Parameters.getPrincessMoveField(princessIndex).forceDraw(canvas);

                canvas.restore();

                canvas.fakeClip(fake.apply(clip));

                Container<DrawImage> images = new Container<>();
                display.addMyselfTo(images);

                images.forceDraw(canvas);

                canvas.fakeRestore();
                canvas.restore();

                break;
        }

        canvas.restore();

        if(statesChangeableValue.get().getTimer().elapsed()) {
            playState.nextPlayState();
        }
    }
}
