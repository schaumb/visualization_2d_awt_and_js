package bxlx.system;

import bxlx.graphics.Point;

/**
 * Created by qqcs on 2017.01.03..
 */
public interface IMouseEventListener {
    void move(Point position);

    void down(Point where, boolean leftButton);

    void up(Point where, boolean leftButton);
}
