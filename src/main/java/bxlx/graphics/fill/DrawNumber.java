package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;

/**
 * Created by qqcs on 2017.01.09..
 */
public class DrawNumber extends ChangeableDrawable {
    private final ChangeableValue<Integer> number;
    private final ChangeableValue<String> suffix;
    private final ChangeableValue<String> referenceText;

    public DrawNumber() {
        this(0);
    }

    public DrawNumber(int number) {
        this(number, "", null);
    }

    public DrawNumber(int number, String suffix, String referenceText) {
        this.number = new ChangeableValue<>(this, number);
        this.suffix = new ChangeableValue<>(this, suffix);
        this.referenceText = new ChangeableValue<>(this, referenceText);
    }

    public ChangeableValue<Integer> getNumber() {
        return number;
    }

    public ChangeableValue<String> getSuffix() {
        return suffix;
    }

    public ChangeableValue<String> getReferenceText() {
        return referenceText;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        new Text(number.get() + suffix.get(), referenceText.get()).forceDraw(canvas);
    }
}

