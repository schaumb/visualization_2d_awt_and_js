package bxlx.system.input.clickable;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Predicate;

/**
 * Created by qqcs on 2017.02.07..
 */
public class ChangeImageClickable extends OnOffClickable {
    private final DrawImage disabledImg;
    private final DrawImage insideImg;
    private final DrawImage normalImg;
    private final DrawImage clickedImg;
    private final Predicate<Color> clickable;

    public ChangeImageClickable(String norm, String ins, String dis, Predicate<Color> clickable) {
        this(norm, ins, dis, norm, clickable);
    }

    public ChangeImageClickable(String norm, String ins, String dis, String clicked, Predicate<Color> clickable) {
        this.disabledImg = new DrawImage(dis);
        this.insideImg = new DrawImage(ins);
        this.normalImg = new DrawImage(norm);
        this.clickedImg = new DrawImage(clicked);
        this.clickable = clickable;
    }

    private DrawImage getActualImage() {
        if (disabled.get()) {
            return disabledImg;
        } else if (inside.get()) {
            return insideImg;
        } else if (getOn().get()) {
            return clickedImg;
        } else {
            return normalImg;
        }
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        getActualImage().forceDraw(canvas);
    }

    @Override
    public boolean isContains(Rectangle bound, Point position) {
        return clickable.test(normalImg.getColor(bound, position));
    }
}