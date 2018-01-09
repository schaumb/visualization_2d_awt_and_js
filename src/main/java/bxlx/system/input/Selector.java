package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.Text;
import bxlx.system.input.clickable.OnOffClickable;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.26..
 */
public class Selector extends SplitContainer<MarginDrawable<Container>> {

    private final ArrayList<Button<OnOffClickable>> list = new ArrayList<>();
    private final ArrayList<Text> texts = new ArrayList<>();
    private final ChangeableDrawable.ChangeableValue<String> getReferenceText;
    private int selectedButtonIndex = -1;

    public Selector(boolean xSplit, boolean hasReferenceText) {
        super(xSplit);
        this.getReferenceText = hasReferenceText ? new ChangeableDrawable.ChangeableValue<>(this, "") : null;
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
        add(margin(new Container<>().add(button).add(new MarginDrawable<>(text, 50, 50))));
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
                    if (b.getChild().get().getOn().get()) {
                        selectedButtonIndex = index;
                        for (int i = 0; i < size(); ++i) {
                            if (i != selectedButtonIndex) {
                                list.get(i).getChild().get().getOn().setElem(false);
                            }
                        }
                    } else {
                        selectedButtonIndex = -1;
                    }
                });
        return this;
    }
}
