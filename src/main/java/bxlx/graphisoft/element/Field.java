package bxlx.graphisoft.element;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphisoft.ElementHolder;
import bxlx.graphisoft.Game;
import bxlx.graphisoft.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by ecosim on 4/27/17.
 */
public class Field extends ChangeableDrawable {
    private int type;
    private final DrawImage fieldImage;
    private Display display;
    private List<Princess> princesses = new ArrayList<>();
    private Point position;

    private boolean moving = false;

    public Field(int type) {
        this.type = type;
        this.fieldImage = new DrawImage(Parameters.imgDir() + type + ".jpg");
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public void setPrincess(Princess princess) {
        princesses.add(princess);
    }

    public void setPosition(Point position) {
        this.position = position;
        for(Princess p : princesses) {
            p.setPosition(position);
        }
    }

    public void setMove() {
        moving = true;
    }

    public void setNoMove() {
        moving = false;
    }

    public int getType() {
        return type;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        fieldImage.forceDraw(canvas);
        if(display != null) {
            display.forceDraw(canvas);
        }

        for(Princess princess : princesses) {
            princess.forceDraw(canvas);
        }
    }

    public boolean isMoving() {
        return moving;
    }

    public void addElements(Field f) {
        f.setDisplay(display);
        for(Princess p : princesses) {
            f.setPrincess(p);
        }
    }

    public void removePrincess(Princess p) {
        princesses.remove(p);
    }
}
