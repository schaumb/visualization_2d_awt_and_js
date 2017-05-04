package bxlx.graphisoft.element;

import bxlx.graphics.Point;
import bxlx.graphics.container.Container;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphisoft.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecosim on 4/27/17.
 */
public class Display {
    private final List<Princess> princesses = new ArrayList<>();
    private boolean active = true;
    private Point position;

    public void setPrincess(Princess princess) {
        princesses.add(princess);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isOn() {
        return !princesses.isEmpty();
    }

    public void deactivate() {
        active = false;
        princesses.clear();
    }

    public void activate() {
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void addMyselfTo(Container<DrawImage> result) {
        result.add(Parameters.getMonitor(isOn()));

        for(Princess princess : princesses) {
            result.add(Parameters.getPrincessButton(princess.getIndex()));
        }
    }
}
