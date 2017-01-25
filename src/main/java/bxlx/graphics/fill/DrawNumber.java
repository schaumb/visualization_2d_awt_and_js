package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNumber extends Text {
    private final ChangeableValue<Integer> number;
    private final ChangeableValue<String> suffix;

    public DrawNumber() {
        this(0);
    }

    public DrawNumber(int number) {
        this(number, "", null, 0);
    }

    public DrawNumber(int number, String suffix, String referenceText, int align) {
        super(null, referenceText, align);
        this.number = new ChangeableValue<>(this, number);
        this.suffix = new ChangeableValue<>(this, suffix);
        getText().setSupplier(() -> this.number.get() + this.suffix.get());
    }

    public ChangeableValue<Integer> getNumber() {
        return number;
    }

    public ChangeableValue<String> getSuffix() {
        return suffix;
    }
}

