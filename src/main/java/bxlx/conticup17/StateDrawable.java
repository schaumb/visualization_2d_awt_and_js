package bxlx.conticup17;

import bxlx.graphics.IDrawable;
import bxlx.graphics.container.SplitContainer;

/**
 * Created by qqcs on 5/8/17.
 */
public class StateDrawable extends SplitContainer<IDrawable> {
    private final RobotStates states;

    public StateDrawable(RobotStates states, RobotStateTimer timer) {
        super(true);
        this.states = states;

        for (int i = 0; i < this.states.getState().getPlayers().length; ++i) {
            add(new OneStateDrawable(states, timer, i));
        }
    }
}
