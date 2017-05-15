package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.combined.Builder;

import java.util.function.Supplier;

/**
 * Created by qqcs on 5/10/17.
 */
public class PacketDrawable extends ChangeableDrawable {
    private final ChangeableValue<RobotStates.Unit> unit;
    private final ChangeableValue<Double> size = new ChangeableValue<>(this, (Double) null);

    public PacketDrawable(RobotStates.Unit unit, double size) {
        this.unit = new ChangeableValue<>(this, unit);
        this.size.setElem(size);
    }

    public PacketDrawable(Supplier<RobotStates.Unit> unit) {
        this.unit = new ChangeableValue<>(this, unit);
    }

    public ChangeableValue<RobotStates.Unit> getUnit() {
        return unit;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        RobotStates.Unit myUnit = unit.get();
        if(myUnit == null)
            return;

        Builder.text(myUnit.getId())
                .makeColored(Color.WHITE)
                .makeClipped(true, r -> r.getScaled(size.get() == null ? 1 : size.get() / r.getSize().getLongerDimension()))
                .makeBackgrounded(myUnit.getTypeColor())
                .get().draw(canvas);
    }
}
