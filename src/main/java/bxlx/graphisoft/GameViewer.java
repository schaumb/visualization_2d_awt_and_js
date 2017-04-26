package bxlx.graphisoft;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.drawable.ColoredDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.fill.MultilineText;
import bxlx.graphics.fill.Text;
import bxlx.system.SystemSpecific;

import java.util.function.Supplier;

/**
 * Created by ecosim on 4/26/17.
 */
public class GameViewer extends DrawableWrapper<ColoredDrawable<MultilineText>> {
    private final ChangeableValue<String> file;
    private final ChangeableValue<String> fileContent;

    public GameViewer(Supplier<String> val) {
        super(new ColoredDrawable<>(new MultilineText(() -> "ASD"), Color.BLACK));
        this.file = new ChangeableValue<>(this, val);
        this.fileContent = new ChangeableValue<>(getChild().get().getChild().get(), "ASDY\ngnjas\ndjlvsdvdcv\n\\ndfssdfdsf");

        getChild().get().getChild().get().getText().setSupplier(fileContent.getAsSupplier());
    }


    @Override
    protected void forceRedraw(ICanvas canvas) {
        if(file.isChanged()) {
            fileContent.setElem("ASDY\ngnjas\ndjlvsdvdcv\n\\ndfssdfdsf\nTestMessage");
            readFile(file.get());
        }
        SystemSpecific.get().log("Redraw gameViewer");
        super.forceRedraw(canvas);
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
        String file = String.format(SystemSpecific.get().getArgs()[1], 2, fileName);
        SystemSpecific.get().readTextFileAsync(from, file, r -> start(fileName, r));
    }

    public void start(String fileName, String file) {
        if(!SystemSpecific.get().equals(fileName, this.file.get())) {
            return;
        }

        if(file == null) {
            SystemSpecific.get().log("Can not reach file: " + fileName + ", try again after 2 sec");
            SystemSpecific.get().runAfter(() -> readFile(fileName), 2000);
            return;
        }
        SystemSpecific.get().log("Success read " + fileName);
        fileContent.setElem(file);
    }
}
