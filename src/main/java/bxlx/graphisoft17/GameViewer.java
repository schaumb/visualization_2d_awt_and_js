package bxlx.graphisoft17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.container.Splitter;
import bxlx.system.SystemSpecific;

import java.util.function.Supplier;

/**
 * Created by ecosim on 4/26/17.
 */
public class GameViewer extends Splitter {
    private final ChangeableDrawable.ChangeableValue<String> file;
    private final ChangeableDrawable.ChangeableValue<StateHolder> stateHolder;
    private PlayState playState = new PlayState();


    public GameViewer(Supplier<String> val, ChangeableDrawable.ChangeableValue<Boolean> settingIsOn) {
        super(true, -600, null, null);
        this.file = new ChangeableDrawable.ChangeableValue<>(this, val);
        this.stateHolder = new ChangeableDrawable.ChangeableValue<>(this, (StateHolder) null);

        getFirst().setElem(
                new Splitter(false, () -> settingIsOn.get() ? -50.0 : 0.0,
                        stateHolder.transform(state -> generateDrawableWith(state, playState))
                                .getAsSupplier(), playState));

        getSecond().setSupplier(stateHolder.transform(state -> generateTeamInfos(state, playState)).getAsSupplier());

    }

    private IDrawable generateTeamInfos(StateHolder state, PlayState playState) {
        return new TeamInfos(state, playState);
    }

    private IDrawable generateDrawableWith(StateHolder state, PlayState playState) {
        return new ElementDrawable(state, playState);
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().setIf(file.isChanged() || stateHolder.isChanged(), IDrawable.Redraw.PARENT_NEED_REDRAW);
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if(file.isChanged()) {
            readFile(file.get());
        }
        super.forceRedraw(canvas);
    }

    public void readFile(String fileName) {
        if(SystemSpecific.get().getArgs().length < 2) {
            SystemSpecific.get().log("No file argument");
            return;
        }
        if(fileName == null || !SystemSpecific.get().isEquals(fileName, file.get())) {
            return;
        }
        stateHolder.setElem(null);
        playState.reset(null);
        String from = SystemSpecific.get().getArgs()[0];
        String file = Game.formatString(SystemSpecific.get().getArgs()[1], "2", fileName);
        SystemSpecific.get().readTextFileAsync(from, file, r -> start(fileName, r));
    }

    public void start(String fileName, String file) {
        if(!SystemSpecific.get().isEquals(fileName, this.file.get())) {
            return;
        }

        String realFileName = Game.formatString(SystemSpecific.get().getArgs()[1], "2", fileName);

        if(file == null) {
            SystemSpecific.get().log("Can not reach file: " + realFileName + ", try again after 2 sec");
            SystemSpecific.get().runAfter(() -> readFile(fileName), 2000);
            return;
        }
        if(file.trim().equals("")) {
            SystemSpecific.get().log("File " + realFileName + " is empty, try again 10 sec");
            SystemSpecific.get().runAfter(() -> readFile(fileName), 10000);
            return;
        }
        if(file.indexOf("<html>") == 0) {
            SystemSpecific.get().log("File " + realFileName + " is not found, try again 10 sec");
            SystemSpecific.get().runAfter(() -> readFile(fileName), 10000);
            return;
        }

        StateHolder newStateHolder = new StateHolder(file);
        stateHolder.setElem(newStateHolder);
        playState.reset(newStateHolder);
    }
}
