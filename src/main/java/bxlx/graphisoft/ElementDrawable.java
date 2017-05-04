package bxlx.graphisoft;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;
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
        SystemSpecific.get().log("Current status is: " + currentState + " and percent: " + percent);

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
        canvas.restore();

        switch (currentState) {
            case BEFORE_PUSH:
                for (List<Field> line : fields) {
                    for (Field field : line) {
                        if (field == null || field.getPosition() == null) {
                            continue;
                        }
                        forceDrawField.accept(field);
                    }
                }

                break;
            case PUSH:

                break;
            case GOTO:
                break;
        }


        if(statesChangeableValue.get().getTimer().elapsed()) {
            playState.nextPlayState();
        }
    }
}
