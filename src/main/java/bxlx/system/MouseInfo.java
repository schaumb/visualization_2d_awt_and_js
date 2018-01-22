package bxlx.system;

import bxlx.graphics.Point;

/**
 * Created by qqcs on 2017.01.03..
 */
public class MouseInfo implements IMouseEventListener {
    private static MouseInfo INSTANCE;
    private ObservableValue<Point> position = new ObservableValue<>(Point.ORIGO);
    private ObservableValue<Boolean> leftClicked = new ObservableValue<>(false);

    private MouseInfo() {
        SystemSpecific.get().setMouseEventListenerQueue(this);
    }

    public static MouseInfo get() {
        if (INSTANCE == null) {
            INSTANCE = new MouseInfo();
        }
        return INSTANCE;
    }

    @Override
    public void move(Point position) {
        this.position.setValue(position);
    }

    @Override
    public void down(Point where, boolean leftButton) {
        if (leftButton) {
            position.setValue(where);
            leftClicked.setValue(true);
        }
    }

    @Override
    public void up(Point where, boolean leftButton) {
        if (leftButton) {
            position.setValue(where);
            leftClicked.setValue(false);
        }
    }

    public ObservableValue<Point> getPosition() {
        return position;
    }

    public ObservableValue<Boolean> isLeftClicked() {
        return leftClicked;
    }
}
