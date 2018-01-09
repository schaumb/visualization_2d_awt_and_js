package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNumber extends Text {
    private final ChangeableDrawable.ChangeableValue<Integer> number;
    private final ChangeableDrawable.ChangeableValue<String> suffix;

    public DrawNumber() {
        this(0);
    }

    public DrawNumber(int number) {
        this(number, "", null, 0);
    }

    public DrawNumber(int number, String suffix, String referenceText, int align) {
        super((String) null, referenceText, align);
        this.number = new ChangeableDrawable.ChangeableValue<>(this, number);
        this.suffix = new ChangeableDrawable.ChangeableValue<>(this, suffix);
        getText().setSupplier(() -> this.number.get() + this.suffix.get());
    }

    public ChangeableDrawable.ChangeableValue<Integer> getNumber() {
        return number;
    }

    public ChangeableDrawable.ChangeableValue<String> getSuffix() {
        return suffix;
    }
}

