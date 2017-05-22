package bxlx.graphisoft17.element;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.Container;
import bxlx.graphisoft17.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecosim on 4/27/17.
 */
public class Field {
    private int type;
    private Display display;
    private List<Princess> princesses = new ArrayList<>();
    private Point position;
    private boolean blocked = false;

    public Field(int type) {
        this.type = type;
    }

    public void setDisplay(Display display) {
        this.display = display;
        if(display != null && position != null) {
            display.setPosition(position);
        }
    }

    public void setPrincess(Princess princess) {
        princesses.add(princess);
        if(position != null) {
            princess.setPosition(position);
        }
    }

    public void setPosition(Point position) {
        this.position = position;
        for(Princess p : princesses) {
            p.setPosition(position);
        }
        if(display != null) {
            display.setPosition(position);
        }
    }

    public int getType() {
        return type;
    }

    public Point getPosition() {
        return position;
    }

    public void moveElements(Field f) {
        if(display != null) {
            f.setDisplay(display);
        }
        for(Princess p : princesses) {
            f.setPrincess(p);
        }
        /*
        display = null;
        clearPrincesses();
        */
    }

    public void clearPrincesses() {
        princesses.clear();
    }

    public void removePrincess(Princess p) {
        princesses.remove(p);
    }

    public Display getDisplay() {
        return display;
    }

    public static boolean hasRouteToStatic(Field from, Field to,
                                           Direction direction) {
        return from.hasRouteTo(direction) && to.hasRouteTo(direction.opposite());
    }

    private boolean hasRouteTo(Direction direction) {
        int ordinal = 0;
        if(direction == Direction.LEFT)
            ordinal = 1;
        if(direction == Direction.DOWN)
            ordinal = 2;
        if(direction == Direction.RIGHT)
            ordinal = 3;

        return (type & (1 << ordinal)) > 0;
    }

    public Container<IDrawable> drawable(Player playerWhosTurn) {
        Container<IDrawable> result = new Container<>();

        result.add(Parameters.getField(getType()));

        if(blocked) {
            result.add(Parameters.blocked());
        }

        if(display != null) {
            if(display == playerWhosTurn.getTargetDisplay()) {
                result.add(Builder.make(Parameters.getPrincessMoveField(playerWhosTurn.getIndex())).makeMargin(0.1).makeBackgrounded(Color.BLACK).makeMargin(0.5).get());
            }
            display.addMyselfTo(result);
        }

        for(Princess princess : princesses) {
            if(princess.getIndex() == playerWhosTurn.getIndex()) {
                result.add(Builder.make(Parameters.getPrincessMoveField(playerWhosTurn.getIndex())).makeMargin(0.1).makeBackgrounded(Color.BLACK).makeMargin(0.5).get());
                break;
            }
        }

        for(Princess princess : princesses) {
            result.add(Parameters.getPrincess(princess.getIndex()));
        }

        return result;
    }

    public void setBlocked() {
        blocked = true;
    }
}
