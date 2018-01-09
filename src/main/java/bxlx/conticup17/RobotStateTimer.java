package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;
import bxlx.system.functional.ValueOrSupplier;
import bxlx.system.input.Button;
import bxlx.system.input.clickable.ColorSchemeClickable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by qqcs on 5/11/17.
 */
public class RobotStateTimer extends ChangeableDrawable {
    private final RobotStates states;
    private Timer timer = new ChangeableDrawable.ChangeableTimer(this, new Timer(1500)).getTimer();
    private final ChangeableDrawable.ChangeableValue<Double> now;
    private final ChangeableDrawable.ChangeableValue<Double> speedSlider;
    public RobotStateTimer(RobotStates states, ChangeableDrawable.ChangeableValue<Double> now, ChangeableDrawable.ChangeableValue<Double> speedSlider) {
        this.states = states;
        this.now = now;
        new ChangeableDrawable.ChangeableValue<>(this, now.getAsSupplier());
        this.speedSlider = speedSlider;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        if(speedSlider.isChanged()) {
            double percent = timer.percent();
            timer.setLength(Math.round(speedSlider.get() * 3000));
            timer.setPercent(percent);
        }

        if(now.isChanged()) {
            states.setTimePercent(now.get());
        }

        if(timer.elapsed()) {
            if(states.setNextState()) {
                timer.setStart();
                now.setElem(Math.max(0, Math.min(1, states.getTimePercent())));
            }
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
