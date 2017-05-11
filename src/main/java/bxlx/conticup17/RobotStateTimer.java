package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.system.Timer;

/**
 * Created by qqcs on 5/11/17.
 */
public class RobotStateTimer extends ChangeableDrawable {
    private final RobotStates states;
    private Timer timer = new Timer(2000);
    public RobotStateTimer(RobotStates states) {
        this.states = states;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        if(timer.elapsed()) {


            if(states.setNextState()) {
                timer.setStart();
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
