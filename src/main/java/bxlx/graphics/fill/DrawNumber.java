package bxlx.graphics.fill;

import bxlx.graphics.drawable.DrawableWrapper;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNumber extends DrawableWrapper<Text> {
    private int number;
    private final String suffix;

    public DrawNumber() {
        this(0);
    }

    public DrawNumber(int number) {
        this(number, "", null);
    }

    public DrawNumber(int number, String suffix, String referenceText) {
        super(new Text(number + suffix, referenceText));
        this.number = number;
        this.suffix = suffix;
    }

    public int getNumber() {
        return number;
    }

    public DrawNumber setNumber(int number) {
        this.number = number;
        getWrapped().setText(number + suffix);
        return this;
    }
}

