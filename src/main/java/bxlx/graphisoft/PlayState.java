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

    public static enum States {
        BEFORE_PUSH(0),
        PUSH(1000),
        WAIT_AFTER_PUSH(200),
        GOTO(1000),
        WAIT_AFTER_GOTO(200),
        END_TURN(0);

        private Timer timer;


        private States(int i) {
            timer = new Timer(i);
        }

        public Timer getTimer() {
            return timer;
        }
    }

    public void nextPlayState() {
        if(stateHolder == null || !state.getTimer().elapsed() || !play.get())
            return;

        switch (state) {
            case BEFORE_PUSH: {
                Player player = stateHolder.getPlayerWhosTurn();
                if (player.getPushMessage() == null) {
                    state = States.END_TURN;
                    break;
                }

                state = States.PUSH;
                break;
            }
            case PUSH:
                state = States.WAIT_AFTER_PUSH;
                stateHolder.finalizePush();
                break;
            case WAIT_AFTER_PUSH: {
                Player player = stateHolder.getPlayerWhosTurn();
                Point princessPosition = stateHolder.getPrincesses().get(stateHolder.getWhosTurn()).getPosition();
                if (player.getGotoMessage() == null || player.getGotoMessage().equals(princessPosition)) {
                    state = States.END_TURN;
                    break;
                }
                state = States.GOTO;
                stateHolder.removePrincess();
                break;
            }
            case GOTO:
                state = States.WAIT_AFTER_GOTO;
                stateHolder.finalizeGoto();
                break;
            case WAIT_AFTER_GOTO:
                state = States.END_TURN;
                break;
            case END_TURN:
                if(stateHolder.hasNextState()) {
                    stateHolder.nextState();
                } else {
                    play.setElem(false);
                }
                state = States.BEFORE_PUSH;
                break;
        }
        state.getTimer().setStart();
    }
}
