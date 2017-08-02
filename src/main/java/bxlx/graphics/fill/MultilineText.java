package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.container.SplitContainer;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;

import java.util.function.Supplier;

/**
 * Created by ecosim on 4/26/17.
 */
public class MultilineText extends ChangeableDrawable {
    private final ChangeableValue<String> text;

    public MultilineText(Supplier<String> text) {
        this.text = new ChangeableValue<>(this, text);
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        SplitContainer<Text> c = new SplitContainer<>();
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
        c.forceDraw(canvas);
    }

    @Override
    public Redraw needRedraw() {
        return super.needRedraw().setIf(text.isChanged(), Redraw.PARENT_NEED_REDRAW);
    }

    public ValueOrSupplier<String> getText() {
        return text;
    }
}
