package bxlx.graphisoft;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphisoft.element.Player;
import bxlx.graphisoft.element.Princess;
import bxlx.system.Timer;

/**
 * Created by ecosim on 2017.05.04..
 */
public class PlayState extends SplitContainer<IDrawable> {
    private ChangeableDrawable.ChangeableValue<Boolean> play = new ChangeableValue<>(this, true);
    private States state;
    private StateHolder stateHolder;

    public void reset(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
        state = States.BEFORE_PUSH;
        play.setElem(true);
    }

    public States getState() {
        return state;
    }

    public void nextPlayState() {
        if(stateHolder == null || !state.getTimer().elapsed() || !play.get()) {
            return;
        }

        if(state == States.BEFORE_PUSH) {
            Player player = stateHolder.getPlayerWhosTurn();
            if (player.getPushMessage() == null) {
                state = States.END_TURN;
            } else {
                state = States.PUSH;

            }
        } else if(state == States.PUSH) {
            state = States.WAIT_AFTER_PUSH;
            stateHolder.finalizePush();
        } else if(state == States.WAIT_AFTER_PUSH) {
                Player player = stateHolder.getPlayerWhosTurn();
                Point princessPosition = stateHolder.getPrincesses().get(stateHolder.getWhosTurn()).getPosition();
                if (player.getGotoMessage() == null || player.getGotoMessage().equals(princessPosition)) {
                    state = States.END_TURN;
                } else {
                    state = States.GOTO;
                    stateHolder.removePrincess();
                }
        } else if(state == States.GOTO) {
            state = States.WAIT_AFTER_GOTO;
            stateHolder.finalizeGoto();
        } else if(state == States.WAIT_AFTER_GOTO) {
            state = States.END_TURN;
        } else if(state == States.END_TURN) {
                if(stateHolder.hasNextState()) {
                    stateHolder.nextState();
                } else {
                    play.setElem(false);
                }
                state = States.BEFORE_PUSH;
        }

        state.getTimer().setStart();
    }
}
