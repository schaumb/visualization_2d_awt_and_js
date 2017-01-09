package bxlx.graphics.fill;

import bxlx.graphics.drawable.DrawableWrapper;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNumber extends DrawableWrapper<Text> {
    private int number;

    public DrawNumber() {
        this(0);
    }

    public DrawNumber(int number) {
        super(new Text(number + ""));
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public DrawNumber setNumber(int number) {
        this.number = number;
        getWrapped().setText(number + "");
        return this;
    }
}

