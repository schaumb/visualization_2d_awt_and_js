package bxlx.conticup17;

import bxlx.graphics.IDrawable;
import bxlx.graphics.container.SplitContainer;

/**
 * Created by qqcs on 5/8/17.
 */
public class StateDrawable extends SplitContainer<OneStateDrawable> {
    private final RobotStates states;

    public StateDrawable(RobotStates states) {
        super(true);
        this.states = states;

        for (RobotStates.RobotPlayer robotPlayer : this.states.getPlayers()) {
            add(new OneStateDrawable(robotPlayer));
        }
    }
}
