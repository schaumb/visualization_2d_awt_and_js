package bxlx.system.input.clickable;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.system.functional.ValueOrSupplier;
import bxlx.system.input.Button;

/**
 * Created by qqcs on 2017.01.25..
 */
public abstract class OnOffClickable extends Button.Clickable {
    protected ValueOrSupplier<Boolean> on = new ValueOrSupplier<>(false);

    @Override
    public void clicked() {
        super.clicked();
        on.setElem(!on.get());
    }

    public ValueOrSupplier<Boolean> getOn() {
        return on;
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return new Redraw().setIf(on.isChanged(), Redraw.I_NEED_REDRAW);
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        on.commit();
    }
}
