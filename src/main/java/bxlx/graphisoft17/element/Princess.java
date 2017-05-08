package bxlx.graphisoft17.element;

import bxlx.graphics.Point;

/**
 * Created by ecosim on 4/27/17.
 */
public class Princess {
    private final int index;
    private Point position;

    public Princess(int index) {
        this.index = index;
    }

    public void setPosition(Point point) {
        this.position = point;
    }

    public Point getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }
}
