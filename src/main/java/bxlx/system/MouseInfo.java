package bxlx.system;

import bxlx.graphics.Point;

/**
 * Created by qqcs on 2017.01.03..
 */
public class MouseInfo implements IMouseEventListener {
    private Point position = Point.ORIGO;
    private boolean leftClicked = false;

    private MouseInfo() {
    }

    @Override
    public void move(Point position) {
        this.position = position;
    }

    @Override
    public void down(Point where, boolean leftButton) {
        position = where;
        leftClicked |= leftButton;
    }

    @Override
    public void up(Point where, boolean leftButton) {
        position = where;
        leftClicked &= !leftButton;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isLeftClicked() {
        return leftClicked;
    }

    private static final MouseInfo INSTANCE = new MouseInfo();

    public static MouseInfo get() {
        return INSTANCE;
    }
}