package bxlx.graphisoft;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.combined.Stick;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.fill.Text;
import bxlx.graphisoft.element.Player;
import bxlx.system.ColorScheme;

/**
 * Created by ecosim on 2017.05.04..
 */
public class TeamInfos extends SplitContainer<IDrawable> {
    private final StateHolder stateHolder;
    private final PlayState playState;

    private final ChangeableDrawable.ChangeableValue<Integer> whoIsTurn;

    public TeamInfos(StateHolder stateHolder, PlayState playState) {
        super(false);
        this.stateHolder = stateHolder;
        this.playState = playState;
        this.whoIsTurn = new ChangeableValue<>(this, () -> stateHolder == null ? null : stateHolder.getWhosTurn());

        if(stateHolder == null)
            return;


        for(int i = 0; i < stateHolder.getPlayers().size(); ++i) {
            int iTh = i;
            Player p = stateHolder.getPlayers().get(i);

            DrawImage img = Parameters.getPrincess(i);
            add(Builder.container(true)
                    .add(new Splitter(true, whoIsTurn.transform(j -> j != iTh ? 0.0 : 1.0).getAsSupplier(), Builder.background().makeColored(Color.WHITE).get(),
                            Builder.make(new Stick(0, 0.2, 0.5, null, new DrawNGon(3, 0))).makeColored(ColorScheme.getCurrentColorScheme().textColor).get()))
                    .add(new Text(p.getName()))
                    .add(new AspectRatioDrawable<>(img, false, 0, 0, () -> img.getOriginalAspectRatio()))
                    .add(new Text(() -> p.getScore() + " ; " + stateHolder.getPointTo(p.getName())[0] + " ; " + stateHolder.getPointTo(p.getName())[1]))
                    .makeMargin(5, 7)
                    .makeColored(Color.BLACK)
                    .get());
        }

    }
}
