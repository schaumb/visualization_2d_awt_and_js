package bxlx.graphics.combined;

import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.drawable.SquareDrawable;

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

    public Builder<SquareDrawable> makeSquare(int alignX, int alignY) {
        return new Builder<>(new SquareDrawable(getWrapped(), alignX, alignY));
    }

    // TODO
}
