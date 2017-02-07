package bxlx.system.input;

import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.system.input.clickable.OnOffClickable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.25..
 */
public class RadioButtons extends SplitContainer<MarginDrawable<Button<OnOffClickable>>> {
    private int selectedButtonIndex = -1;

    public RadioButtons(boolean xSplit) {
        super(xSplit);
    }

    public Supplier<Integer> getSelected() {
        return () -> selectedButtonIndex;
    }

    public static MarginDrawable<Button<OnOffClickable>> margin(Button<OnOffClickable> button) {
        return new MarginDrawable<>(button, 0, 0.1);
    }

    public RadioButtons add(Button<OnOffClickable> button) {
        final int index = size();
        add(margin(button));
        final Consumer<Button<OnOffClickable>> prevConsumer = button.getAtClick().get();
        button.getAtClick().setElem(
                b -> {
                    if (prevConsumer != null) {
                        prevConsumer.accept(b);
                    }
                    if (b.getChild().get().getOn().get()) {
                        selectedButtonIndex = index;
                        for (int i = 0; i < size(); ++i) {
                            if (i != selectedButtonIndex) {
                                get(i).get().getChild().get().getChild().get().getOn().setElem(false);
                            }
                        }
                    } else {
                        selectedButtonIndex = -1;
                    }
                });
        return this;
    }

    public RadioButtons add(OnOffClickable clickable) {
        return add(new Button<>(clickable, null, null, () -> false));
    }
}
