package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.Container;
import bxlx.system.SystemSpecific;

import java.util.HashMap;
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

    private HashMap<String, String> players = new HashMap<>();

    public PlayerPointDrawable(RobotStates states, int playerNum) {
        super();
        players.put("U4IvbV2plbAO","c_sharks");
        players.put("RHyyXSr2OMN5","mighty_ducks");
        players.put("am17G2JGIg19","anip");
        players.put("gDa16nsvVaAT","git_hap");
        players.put("EEOUw1OiProl","digital_hammer");
        players.put("arKcBTTB1rch","beni_es_a_villanyosok");
        players.put("xuwB9QDIVLsK","utolso");
        players.put("Ag6vCNFTJu9A","petya_es_a_tobbiek");
        players.put("HyMLvvk1sIKV","omroff");
        players.put("BVe1wrvIuzvW","scaliermaelstrom");

        this.states = states;
        this.playerNum = playerNum;

        this.ready = new ChangeableValue<>(this, () -> states.getState() == null ? null : states.getState().getPlayers()[playerNum].isIdentified());
        this.name = new ChangeableValue<>(this, () -> states.getState() == null ? null : states.getState().getPlayers()[playerNum].getName());
        this.scores = new ChangeableValue<>(this, () -> states.getState() == null ? null : states.getState().getPlayers()[playerNum].getScores());

        add(Builder.background().makeColored(Color.WHITE).makeMargin(0.1/5)
                .makeBackgrounded(Color.CONTI_COLOR).makeMargin(0.1).get());

        Supplier<String> maxNameWidth = () -> "beni_es_a_villanyosok";

        if(players.get(states.getState().getPlayers()[playerNum].getName()) == null) {
            SystemSpecific.get().log("NO PLAYER HASH " + states.getState().getPlayers()[playerNum].getName());
        }

        add(Builder.container(false)
                .add(Builder.text(() -> states.getState() == null ? cachedName : (cachedName = (cachedIsIdentified = states.getState().getPlayers()[playerNum].isIdentified()) ? players.get(states.getState().getPlayers()[playerNum].getName()) : "NOT CONN"), maxNameWidth, -1)
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
