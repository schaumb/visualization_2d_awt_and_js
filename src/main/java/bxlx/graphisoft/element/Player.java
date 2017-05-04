package bxlx.graphisoft.element;

import bxlx.graphics.Point;

import java.util.List;

/**
 * Created by ecosim on 4/27/17.
 */
public class Player {
    private final String name;
    private final int index;
    private Field field;
    private int score = 0;
    private String message;
    private List<Display> displays;
    private PushMessage latestPushMessage;
    private Point latestGotoMessage;

    public Player(int index, String name) {
        this.name = name;
        this.index = index;
    }

    public void setDisplays(List<Display> displays) {
        this.displays = displays;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setExtra(Field field) {
        this.field = field;
    }

    public void setZeroCommands() {
        latestPushMessage = null;
        latestGotoMessage = null;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPushMessage(PushMessage pushMessage) {
        latestPushMessage = pushMessage;
    }

    public void setGotoMessage(Point point) {
        this.latestGotoMessage = point;
    }

    public PushMessage getPushMessage() {
        return latestPushMessage;
    }

    public Point getGotoMessage() {
        return latestGotoMessage;
    }

    public void commitPushMessage() {
        setExtra(new Field(getPushMessage().fieldType));
    }

    public static class PushMessage {
        private final boolean column;
        private final boolean positive;
        private final int index;
        private final int fieldType;

        public PushMessage(boolean column, boolean positive, int index, int fieldType) {
            this.column = column;
            this.positive = positive;
            this.index = index;
            this.fieldType = fieldType;
        }

        public boolean isColumn() {
            return column;
        }

        public boolean isPositive() {
            return positive;
        }

        public int getIndex() {
            return index;
        }

        public int getFieldType() {
            return fieldType;
        }
    }

}
