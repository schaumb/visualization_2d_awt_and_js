package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.container.SplitContainer;
import bxlx.system.ColorScheme;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;

import java.util.function.Supplier;

/**
 * Created by ecosim on 4/26/17.
 */
public class MultilineText extends ChangeableDrawable {
    private final ChangeableDrawable.ChangeableValue<String> text;
    private final ChangeableDrawable.ChangeableValue<SplitContainer<Text>> texts;

    public MultilineText(Supplier<String> text) {
        this.text = new ChangeableDrawable.ChangeableValue<>(this, text);
        this.texts = new ChangeableDrawable.ChangeableValue<>(this, new SplitContainer<>());
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        SplitContainer<Text> c = texts.get();
        if(text.isChanged()) {
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

            if(c.size() > lines.length) {
                c = texts.setElem(new SplitContainer<>()).get();
            }
            int where = 0;
            for(; where < Math.min(100, c.size()); ++where) {
                Text t = c.get(where).get();
                t.getText().setElem(lines[where]);
                t.getReferenceText().setElem(longest);
            }

            for(; where < Math.min(100, lines.length); ++where) {
                c.add(new Text(lines[where], longest, -1));
            }
        }
        c.forceDraw(canvas);
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().orIf(true, texts.get().needRedraw()).setIf(text.isChanged(), IDrawable .Redraw.PARENT_NEED_REDRAW);
    }

    public ValueOrSupplier<String> getText() {
        return text;
    }
}
