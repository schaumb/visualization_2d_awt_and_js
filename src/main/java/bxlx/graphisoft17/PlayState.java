package bxlx.graphisoft17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphisoft17.element.Player;
import bxlx.system.input.Button;
import bxlx.system.input.Slider;
import bxlx.system.input.clickable.ColorSchemeClickable;

/**
 * Created by ecosim on 2017.05.04..
 */
public class PlayState extends SplitContainer<IDrawable> {
    private ChangeableDrawable.ChangeableValue<Boolean> play = new ChangeableValue<>(this, true);
    private States state;
    private StateHolder stateHolder;

    private Slider sliderTime0 = new Slider(new Button<>(new ColorSchemeClickable(true, false), null, null, null), true, 0);
    private Slider sliderTime1 = new Slider(new Button<>(new ColorSchemeClickable(true, false), null, null, null), true, 0.5);
    private Slider sliderTime2 = new Slider(new Button<>(new ColorSchemeClickable(true, false), null, null, null), true, 0.1);
    private Slider sliderTime3 = new Slider(new Button<>(new ColorSchemeClickable(true, false), null, null, null), true, 0.5);
    private Slider sliderTime4 = new Slider(new Button<>(new ColorSchemeClickable(true, false), null, null, null), true, 0.1);


    public PlayState() {
        super(true);

        add(sliderTime0);
        add(sliderTime1);
        add(sliderTime2);
        add(sliderTime3);
        add(sliderTime4);

    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if(sliderTime0.getNow().isChanged()) {
            States.BEFORE_PUSH.getTimer().setLength((long) (sliderTime0.getNow().get() * 1500));
        }
        if(sliderTime1.getNow().isChanged()) {
            States.PUSH.getTimer().setLength((long) (sliderTime1.getNow().get() * 1500));
        }
        if(sliderTime2.getNow().isChanged()) {
            States.WAIT_AFTER_PUSH.getTimer().setLength((long) (sliderTime2.getNow().get() * 1500));
        }
        if(sliderTime3.getNow().isChanged()) {
            States.GOTO.getTimer().setLength((long) (sliderTime3.getNow().get() * 1500));
        }
        if(sliderTime4.getNow().isChanged()) {
            States.WAIT_AFTER_GOTO.getTimer().setLength((long) (sliderTime4.getNow().get() * 1500));
        }

        super.forceRedraw(canvas);
    }

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
