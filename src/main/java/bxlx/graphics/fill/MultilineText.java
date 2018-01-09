package bxlx.graphics.fill;

import bxlx.graphics.ICanvas;
import bxlx.graphics.Drawable;
import bxlx.graphics.container.SplitContainer;
import bxlx.system.*;

import java.util.function.Supplier;

/**
 * Created by ecosim on 4/26/17.
 */
public class MultilineText extends Drawable {
    private final ObservableValue<String> text;

    public MultilineText(ObservableValue<String> text) {
        this.text = text;

        text.addObserver((observable, from) -> setRedraw());
    }

    @Override
    protected void forceDraw(ICanvas canvas) {
        SplitContainer<Text> c = new SplitContainer<>(false);
        String[] lines = text.get().split("\n");

        String longest = "i";
        int longestSize = 0;
        for(String line : lines) {
            int lineSize = SystemSpecific.get().stringLength(ColorScheme.getCurrentColorScheme().font, line);
            if(lineSize > longestSize) {
                longest = line;
                longestSize = lineSize;
            }
        }


        for(int where = 0; where < Math.min(100, lines.length); ++where) {
            c.add(new Text(lines[where], longest, -1));
        }
        c.setRedraw(this);
        c.draw(canvas);
    }
}
