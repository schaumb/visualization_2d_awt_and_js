package bxlx.graphics.drawable;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.system.Timer;

public class TimerDrawable extends ChangeableDrawable {
    private final Timer timer;
    private final ChangeableValue<Double> timerPercent;
    private final ChangeableValue<Double> stopPercent;


    public TimerDrawable(int time) {
        this(new Timer(time));
    }

    public TimerDrawable(Timer timer) {
        this.timer = timer;
        this.timerPercent = new ChangeableValue<>(this, () -> timer.percent());
        this.stopPercent = new ChangeableValue<>(this, -1.0);
    }

    public double percent() {
        double stopPercent = this.stopPercent.get();
        return stopPercent < 0.0 ? timer.percent() : stopPercent;
    }

    @Override
    public Redraw needRedraw() {
        return new Redraw().orIf(stopPercent.isChanged(), super.needRedraw())
                .setIf(stopPercent.get() < 0.0, Redraw.I_NEED_REDRAW)
                .setIf(stopPercent.get() < 0.0, Redraw.PARENT_NEED_REDRAW);
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if(timer.elapsed() && stopPercent.get() < 0.0) {
            timer.setStart();
        }
    }

    public void stop() {
        stopPercent.setElem(timer.percent());
        timerPercent.setElem(0.0);
    }

    public void restart() {
        timer.setPercent(stopPercent.get());
        stopPercent.setElem(-1.0);
        timerPercent.setSupplier(() -> timer.percent());
    }
}
