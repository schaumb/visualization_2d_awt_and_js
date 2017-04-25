package bxlx.system.input.clickable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.system.input.Button;

/**
 * Created by qqcs on 2017.01.25..
 */
public abstract class OnOffClickable extends Button.Clickable {
    protected ChangeableValue<Boolean> on = new ChangeableDrawable.ChangeableValue<>(this, false);
    private final boolean onlyOn;

    protected OnOffClickable() {
        this(false);
    }

    protected OnOffClickable(boolean onlyOn) {
        this.onlyOn = onlyOn;
    }

    @Override
    public void clicked() {
        super.clicked();
        on.setElem(onlyOn || !on.get());
    }

    public ChangeableValue<Boolean> getOn() {
        return on;
    }
}
