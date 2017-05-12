package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.Container;
import bxlx.system.SystemSpecific;

import java.util.function.Supplier;

/**
 * Created by qqcs on 5/12/17.
 */
public class PlayerPointDrawable extends Container<IDrawable> {
    private final RobotStates states;
    private final int playerNum;

    private final ChangeableDrawable.ChangeableValue<Boolean> ready;
    private final ChangeableValue<String> name;
    private final ChangeableValue<String> scores;

    private boolean cachedIsIdentified;
    private String cachedStringLength;
    private String cachedName;
    private String cachedScores;


    public PlayerPointDrawable(RobotStates states, int playerNum) {
        super();
        this.states = states;
        this.playerNum = playerNum;

        this.ready = new ChangeableValue<>(this, () -> states.getState() == null ? null : states.getState().getPlayers()[playerNum].isIdentified());
        this.name = new ChangeableValue<>(this, () -> states.getState() == null ? null : states.getState().getPlayers()[playerNum].getName());
        this.scores = new ChangeableValue<>(this, () -> states.getState() == null ? null : states.getState().getPlayers()[playerNum].getScores());

        add(Builder.background().makeColored(Color.WHITE).makeMargin(0.1/5)
                .makeBackgrounded(Color.CONTI_COLOR).makeMargin(0.1).get());

        Supplier<String> maxNameWidth = () -> {
            if(states.getState() == null || !states.getState().isChangedName())
                return cachedStringLength;

            RobotStates.RobotPlayer[] players = states.getState().getPlayers();
            String longestTeamName = players[0].getName();
            for (int i = 1; i < players.length; ++i) {
                if (SystemSpecific.get().stringLength(null, players[i].getName()) >
                        SystemSpecific.get().stringLength(null, longestTeamName)) {
                    longestTeamName = players[i].getName();
                }
            }
            return cachedStringLength = longestTeamName;
        };

        add(Builder.container(false)
                .add(Builder.text(() -> states.getState() == null ? cachedName : (cachedName = (cachedIsIdentified = states.getState().getPlayers()[playerNum].isIdentified()) ? states.getState().getPlayers()[playerNum].getName() : "NOT CONN"), maxNameWidth, -1)
                        .makeColored(() -> (states.getState() == null ? cachedIsIdentified : (cachedIsIdentified = states.getState().getPlayers()[playerNum].isIdentified())) ? Color.CONTI_COLOR : Color.RED )
                        .makeMargin(0.1).get())
                .add(Builder.container(true)
                        .add(Builder.text("ER100", "ER100", -1).makeColored(Color.CONTI_COLOR).get())
                        .add(Builder.text(() -> {
                            String point = (states.getState() == null ? cachedScores : (cachedScores = states.getState().getPlayers()[playerNum].getScores())).split(";")[0];
                            return point.substring(point.indexOf(" ") + 1, point.indexOf(","));
                        }, () -> "ER100", 1).makeColored(Color.GREEN).get())
                        .add(Builder.text(() -> {
                            String point = (states.getState() == null ? cachedScores : (cachedScores = states.getState().getPlayers()[playerNum].getScores())).split(";")[1];
                            return point.substring(point.indexOf(" ") + 1, point.indexOf(","));
                        }, () -> "ER100", 1).makeColored(Color.RED).get())
                        .makeMargin(0.1).get())
                .add(Builder.container(true)
                        .add(Builder.text("ERC1", "ER100", -1).makeColored(Color.CONTI_COLOR).get())
                        .add(Builder.text(() -> {
                            String point = (states.getState() == null ? cachedScores : (cachedScores = states.getState().getPlayers()[playerNum].getScores())).split(";")[0];
                            return point.substring(1 + point.indexOf(","));
                        }, () -> "ER100", 1).makeColored(Color.GREEN).get())
                        .add(Builder.text(() -> {
                            String point = (states.getState() == null ? cachedScores : (cachedScores = states.getState().getPlayers()[playerNum].getScores())).split(";")[1];
                            return point.substring(1 + point.indexOf(","));
                        }, () -> "ER100", 1).makeColored(Color.RED).get())
                        .makeMargin(0.1).get())
                .add((IDrawable) null)
                .makeMargin(0.1).get());
    }
}
