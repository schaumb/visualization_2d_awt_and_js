package bxlx.graphisoft.element;

import bxlx.graphics.fill.DrawImage;
import bxlx.graphisoft.Parameters;

/**
 * Created by ecosim on 4/27/17.
 */
public class Player {
    private final String name;
    private final DrawImage moveImage;
    private Field field;
    private int score = 0;
    private String message;

    private boolean moving = false;
    private Field goes;
    private Field comes;

    public Player(int index, String name) {
        this.name = name;
        this.moveImage = new DrawImage(Parameters.imgDir() + "q" + index + ".png");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addExtra(int extra) {
        field = new Field(extra);
    }

    public void setMove(int comes, int goes) {
        moving = true;
        this.comes = new Field(comes);
        this.goes = new Field(goes);
    }

    public void setNoMove() {
        moving = false;
    }
}
