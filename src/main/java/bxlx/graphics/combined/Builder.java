package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.DrawArc;
import bxlx.graphics.fill.DrawRectangle;
import bxlx.graphics.fill.SplitContainer;
import bxlx.graphics.fill.Text;

import java.util.ArrayList;

/**
 * Created by qqcs on 2017.01.11..
 */
public class Builder<T extends IDrawable> extends DrawableWrapper<T> {
    private Builder(T wrapped) {
        super(wrapped);
    }

    public static <T extends IDrawable> Builder<T> make(T start) {
        if (start == null) {
            return null;
        }
        return new Builder<>(start);
    }

    public Builder<ColoredDrawable> makeColored(Color color) {
        return new Builder<>(new ColoredDrawable(getWrapped(), color));
    }

    public Builder<MarginDrawable> makeMargin(double margin) {
        return new Builder<>(new MarginDrawable(getWrapped(), margin));
    }

    public Builder<MarginDrawable> makeMargin(double marginX, double marginY) {
        return new Builder<>(new MarginDrawable(getWrapped(), marginX, marginY));
    }

    public Builder<AspectRatioDrawable> makeSquare(int alignX, int alignY) {
        return new Builder<>(new AspectRatioDrawable(getWrapped(), alignX, alignY, () -> 1.0));
    }

    public static Builder<DrawRectangle> background() {
        return new Builder<>(new DrawRectangle());
    }

    public static Builder<Text> text(String text) {
        return new Builder<>(new Text(text));
    }

    public static Builder<DrawArc> circle(boolean inside) {
        return new Builder<>(DrawArc.circle(inside));
    }

    public static Builder<SplitContainer> container(boolean xSplit) {
        return new Builder<>(new SplitContainer(xSplit));
    }

    public static Builder<Container> container() {
        return new Builder<>(new Container(new ArrayList<>()));
    }

    // TODO
}
