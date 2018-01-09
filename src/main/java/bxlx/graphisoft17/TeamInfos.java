package bxlx.graphisoft17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.combined.Stick;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.Text;
import bxlx.graphisoft17.element.Player;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;

import java.util.HashMap;

/**
 * Created by ecosim on 2017.05.04..
 */
public class TeamInfos extends SplitContainer<IDrawable> {
    private final StateHolder stateHolder;
    private final PlayState playState;

    private final ChangeableDrawable.ChangeableValue<Integer> whoIsTurn;
    private final ChangeableDrawable.ChangeableValue<Integer> tickInfos;

    public TeamInfos(StateHolder stateHolder, PlayState playState) {
        super(false);
        this.stateHolder = stateHolder;
        this.playState = playState;
        this.whoIsTurn = new ChangeableDrawable.ChangeableValue<>(this, () -> stateHolder == null ? null : stateHolder.getWhosTurn());
        this.tickInfos = new ChangeableDrawable.ChangeableValue<>(this, () -> stateHolder == null ? null : stateHolder.getTick());

        if(stateHolder == null)
            return;

        String longestText = "i";
        int longestLength = 0;
        for(int i = 0; i < stateHolder.getPlayers().size(); ++i) {
            String name = stateHolder.getPlayers().get(i).getName();
            name = name.substring(0, Math.min(name.length(), 7));
            int length = SystemSpecific.get().stringLength(null, name);

            if(length > longestLength) {
                longestLength = length;
                longestText = name;
            }
        }


        for(int i = 0; i < stateHolder.getPlayers().size(); ++i) {
            int iTh = i;
            Player p = stateHolder.getPlayers().get(i);

            DrawImage img = Parameters.getPrincess(i);
            add(new Splitter(true, 50, new Splitter(true, whoIsTurn.transform(j -> j != iTh ? 0.0 : 1.0).getAsSupplier(), Builder.background().makeColored(Color.WHITE).get(),
                    Builder.make(new Stick(0, 0.4, 0.8, null, new DrawNGon(3, 0))).makeColored(ColorScheme.getCurrentColorScheme().textColor).get()),
                    Builder.container(true)
                    .add(new Text(p.getName().substring(0, Math.min(p.getName().length(), 7)), longestText, -1))
                    .add(new ClippedDrawable<>(new AspectRatioDrawable<>(img, false, 0, 0, () -> img.getOriginalAspectRatio()), true,
                            r -> r.getScaled(2.7)))
                    .add(Builder.container()
                            .add(Builder.container().add(() -> stateHolder.getPlayers().get(iTh).getField().drawable(stateHolder.getPlayers().get(iTh)))
                                .makeClipped(true, c -> c.withStart(c.getStart().add(stateHolder.getMoveDirection() == null || stateHolder.getWhosTurn() != iTh ? Point.ORIGO :
                                        stateHolder.getMoveDirection().multiple(c.getSize().asPoint().multiple(playState.getState().getTimer().percent()))))).get())
                            .add(Builder.container().add(() -> stateHolder.getPlayers().get(iTh).getFieldTo() == null ? null :
                                    stateHolder.getPlayers().get(iTh).getFieldTo().drawable(stateHolder.getPlayers().get(iTh)))
                                    .makeClipped(true, c -> c.withStart(c.getStart().add(stateHolder.getMoveDirection() == null || stateHolder.getWhosTurn() != iTh ? Point.ORIGO :
                                            stateHolder.getMoveDirection().multiple(c.getSize().asPoint().multiple(playState.getState().getTimer().percent() - 1))))).get())
                            .makeAspect(0, 0, 1).get())
                    .add(Builder.make(
                            new Text(() -> p.getScore() + " ; " + stateHolder.getPointTo(p.getName())[0] + " ; " + stateHolder.getPointTo(p.getName())[1], "10 ; 1000 ; 100", 0))
                            .makeBackgrounded(Color.WHITE)
                            .get())
                    .makeMargin(5, 7)
                    .makeColored(Color.BLACK)
                    .get()));
        }
        add(Builder.container(true)
                .add(Builder.text(() -> stateHolder.getTick() + " / " + stateHolder.getMaxTick(),
                        stateHolder.getMaxTick() + " / " + stateHolder.getMaxTick(), 1)
                        .makeMargin(20).get())
                .add(Builder.make(new Stick(0, 0.2, 0.2, null, null))
                        .get())
                .makeColored(ColorScheme.getCurrentColorScheme().textColor)
                .makeBackgrounded(Color.WHITE)
                .get());

        if(stateHolder.getPlayers().size() < 10) {

            longestText = "i";
            longestLength = 0;
            for (HashMap.Entry<String, int[]> entry : stateHolder.getAllPoints().entrySet()) {
                boolean contains = false;

                for(int i = 0; i < stateHolder.getPlayers().size(); ++i) {
                    if(entry.getKey().equals(stateHolder.getPlayers().get(i).getName())) {
                        contains = true;
                        break;
                    }
                }

                if (!contains) {
                    String name = entry.getKey();
                    name = name.substring(0, Math.min(name.length(), 7)) + " " + entry.getValue()[0] + " ; " + entry.getValue()[1];
                    int length = SystemSpecific.get().stringLength(null, name);

                    if (length > longestLength) {
                        longestLength = length;
                        longestText = name;
                    }
                }
            }
            for (HashMap.Entry<String, int[]> entry : stateHolder.getAllPoints().entrySet()) {
                boolean contains = false;

                for(int i = 0; i < stateHolder.getPlayers().size(); ++i) {
                    if(entry.getKey().equals(stateHolder.getPlayers().get(i).getName())) {
                        contains = true;
                        break;
                    }
                }

                if (!contains) {
                    add(Builder.container(true)
                            .add(Builder.container(true)
                                    .add((IDrawable) null)
                                    .add(new Text(entry.getKey().substring(0, Math.min(entry.getKey().length(), 7)) + " " + entry.getValue()[0] + " ; " + entry.getValue()[1],
                                            longestText, -1))
                                    .add((IDrawable) null)
                                    .makeColored(Color.BLACK)
                                    .get())
                            .get());
                }
            }
        }
    }
}
