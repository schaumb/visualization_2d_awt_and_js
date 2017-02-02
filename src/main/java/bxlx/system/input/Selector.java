package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Container;
import bxlx.graphics.fill.Rect;
import bxlx.graphics.fill.SplitContainer;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.26..
 */
public class Selector extends SplitContainer<MarginDrawable<Container>> {
    public static class RectClickable extends OnOffClickable {
        private final Rect rect = new Rect();
        private final MarginDrawable<Rect> smallerRect = new MarginDrawable<>(rect, 5, 5);

        private final Color outsideColor;
        private final Color color;
        private final Color insideColor;
        private final Color clickedColor;
        private final Color disabledColor;
        private final Color containsColor;
        private final Color containsDisabledColor;

        public RectClickable() {
            this(Color.BLACK, Color.GRAY, Color.DARK_GRAY, Color.DARK_GRAY.getScale(Color.BLACK, 0.5), Color.LIGHT_GRAY, Color.BLACK, Color.WHITE);
        }

        public RectClickable(Color outsideColor, Color color, Color insideColor, Color clickedColor, Color disabledColor, Color containsColor, Color containsDisabledColor) {
            this.outsideColor = outsideColor;
            this.color = color;
            this.insideColor = insideColor;
            this.clickedColor = clickedColor;
            this.disabledColor = disabledColor;
            this.containsColor = containsColor;
            this.containsDisabledColor = containsDisabledColor;
        }

        @Override
        public IDrawable.Redraw needRedraw() {
            return super.needRedraw();
        }

        @Override
        public void forceRedraw(ICanvas canvas) {
            super.forceRedraw(canvas);
            canvas.setColor(outsideColor);
            rect.forceDraw(canvas);
            canvas.setColor(disabled.get() ? disabledColor : inside.get() ? insideColor : isOn().get() ? clickedColor : color);
            smallerRect.forceDraw(canvas);
            canvas.setColor(disabled.get() ? containsDisabledColor : containsColor);
        }

        @Override
        public boolean isContains(Rectangle bound, Point position) {
            return rect.isContains(bound, position);
        }
    }

    private final ArrayList<Button<OnOffClickable>> list = new ArrayList<>();
    private final ChangeableDrawable.ChangeableValue<String> getReferenceText;
    private int selectedButtonIndex = -1;

    public Selector(boolean xSplit, boolean hasReferenceText) {
        super(xSplit);
        this.getReferenceText = hasReferenceText ? new ChangeableValue<>(this, "") : null;
    }

    public Supplier<Integer> getSelected() {
        return () -> selectedButtonIndex;
    }

    public static MarginDrawable<Container> margin(Container container) {
        return new MarginDrawable<>(container, 0.002, 0.1);
    }

    public Selector addText(OnOffClickable clickable, Text text) {
        final int index = size();
        final Button<OnOffClickable> button = new Button<>(clickable, null, null, () -> false);
        add(margin(new Container().add(button).add(new MarginDrawable<>(text, 10, 10))));
        list.add(button);
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
