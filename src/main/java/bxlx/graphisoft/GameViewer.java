package bxlx.graphisoft;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.system.SystemSpecific;

import java.util.function.Supplier;

/**
 * Created by ecosim on 4/26/17.
 */
public class GameViewer extends DrawableWrapper<StateHolder> {
    private final ChangeableDrawable.ChangeableValue<String> file;
    private final ChangeableValue<String> fileContent;

    public GameViewer(Supplier<String> val) {
        super(new StateHolder());
        this.file = new ChangeableValue<>(this, val);
        this.fileContent = new ChangeableValue<>(this, "");
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        if(file.isChanged()) {
            fileContent.setElem("");
            readFile(file.get());
        }
        super.forceRedraw(canvas);
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().setIf(file.isChanged() || fileContent.isChanged(), Redraw.PARENT_NEED_REDRAW);
    }

    public void readFile(String fileName) {
        if(SystemSpecific.get().getArgs().length < 2) {
            SystemSpecific.get().log("No file argument");
            return;
        }
        if(fileName == null || !SystemSpecific.get().equals(fileName, file.get())) {
            return;
        }
        String from = SystemSpecific.get().getArgs()[0];
        String file = Game.formatString(SystemSpecific.get().getArgs()[1], "2", fileName);
        SystemSpecific.get().readTextFileAsync(from, file, r -> start(fileName, r));
    }

    public void start(String fileName, String file) {
        if(!SystemSpecific.get().equals(fileName, this.file.get())) {
            return;
        }

        getChild().get().removeElements();
        if(file == null) {
            SystemSpecific.get().log("Can not reach file: " + fileName + ", try again after 2 sec");
            SystemSpecific.get().runAfter(() -> readFile(fileName), 2000);
            return;
        }
        fileContent.setElem(file);

        String[] states = file.split("PLAYERTURN ");

        getChild().get().createElements(states);
    }
}
