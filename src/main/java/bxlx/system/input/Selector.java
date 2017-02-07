package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.SplitContainer;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ColorScheme;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.26..
 */
public class Selector extends SplitContainer<MarginDrawable<Container>> {
    public static class RectClickable extends OnOffClickable {
        private final Rect rect = new Rect();
        private final MarginDrawable<Rect> smallerRect = new MarginDrawable<>(rect, 5, 5);

        @Override
        public IDrawable.Redraw needRedraw() {
            return super.needRedraw();
        }

        @Override
        public void forceRedraw(ICanvas canvas) {
            ColorScheme colors = ColorScheme.getCurrentColorScheme();
            super.forceRedraw(canvas);
            canvas.setColor(colors.buttonBorderColor);

            rect.forceDraw(canvas);
            canvas.setColor(disabled.get() ? colors.disabledColor : inside.get() ? colors.insideColor : isOn().get() ? colors.clickedColor : colors.buttonColor);
            smallerRect.forceDraw(canvas);
            canvas.setColor(colors.buttonTextColor);
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return rect.isContains(bound, position);
        }
    }

    private final ArrayList<Button<OnOffClickable>> list = new ArrayList<>();
    private final ArrayList<Text> texts = new ArrayList<>();
    private final ChangeableDrawable.ChangeableValue<String> getReferenceText;
    private int selectedButtonIndex = -1;

    public Selector(boolean xSplit, boolean hasReferenceText) {
        super(xSplit);
        this.getReferenceText = hasReferenceText ? new ChangeableValue<>(this, "") : null;
    }

    public Supplier<Integer> getSelected() {
        return () -> selectedButtonIndex;
    }

    public Supplier<String> getSelectedText() {
        return () -> selectedButtonIndex < 0 ? null : texts.get(selectedButtonIndex).getText().get();
    }

    public static MarginDrawable<Container> margin(Container container) {
        return new MarginDrawable<>(container, 0.002, 0.1);
    }

    public Selector addText(OnOffClickable clickable, Text text) {
        final int index = size();
        final Button<OnOffClickable> button = new Button<>(clickable, null, null, () -> false);
        add(margin(new Container().add(button).add(new MarginDrawable<>(text, 50, 50))));
        list.add(button);
        texts.add(text);
        if (getReferenceText != null) {
            // TODO measure with real xSize like SystemSpecific.get().stringLength(null, ...);
            int prevSize = getReferenceText.get().length();
            int nowSize = text.getText().get().length();
            if (nowSize > prevSize) {
                getReferenceText.setElem(text.getText().get());
            }

            text.getReferenceText().setSupplier(getReferenceText.getAsSupplier());
        }
        button.getAtClick().setElem(
                b -> {
                    if (b.getChild().get().isOn().get()) {
                        selectedButtonIndex = index;
                        for (int i = 0; i < size(); ++i) {
                            if (i != selectedButtonIndex) {
                                list.get(i).getChild().get().on.setElem(false);
                            }
                        }
                    } else {
                        selectedButtonIndex = -1;
                    }
                });
        return this;
    }
}
