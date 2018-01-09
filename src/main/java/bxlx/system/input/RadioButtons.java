package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.IDrawable;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.container.TransformerContainer;
import bxlx.system.functional.ValueOrSupplier;
import bxlx.system.input.clickable.OnOffClickable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.25..
 */
public class RadioButtons<X extends IDrawable, C extends SplitContainer<X>, O extends OnOffClickable, B extends Button<O>> extends
        TransformerContainer<B, X, C> {
    private int selectedButtonIndex = -1;
    private final List<ChangeableDrawable.ChangeableValue<B>> buttons = new ArrayList<>();

    public RadioButtons(C container, Function<B, X> transformFunction) {
        super(container, transformFunction);
    }

    public Supplier<Integer> getSelected() {
        return () -> selectedButtonIndex;
    }

    public ChangeableDrawable.ChangeableValue<B> getButton(int index) {
        return buttons.get(index);
    }

    public RadioButtons<X, C, O, B> addButton(B button) {
        final int index = size();
        final Consumer<Button<O>> prevConsumer = button.getAtClick().get();
        button.getAtClick().setElem(
                b -> {
                    if (prevConsumer != null) {
                        prevConsumer.accept(b);
                    }
                    ValueOrSupplier<Boolean> boolX = b.getChild().get().getOn();
                    if(!boolX.isChanged()) {
                        return;
                    }
                    if (boolX.get()) {
                        selectedButtonIndex = index;
                        for (int i = 0; i < size(); ++i) {
                            if (i != selectedButtonIndex) {
                                ValueOrSupplier<Boolean> bool = buttons.get(i).get().getChild().get().getOn();
                                if(bool.get()) {
                                    bool.setElem(false);
                                    RadioButtons.this.getChild().get().get(i).get().setRedraw();
                                }
                            }
                        }
                    } else {
                        selectedButtonIndex = -1;
                    }
                });
        ChangeableDrawable.ChangeableValue<B> buttonChangeableValue = new ChangeableDrawable.ChangeableValue<>(this, button);
        buttons.add(buttonChangeableValue);
        super.addVal(buttonChangeableValue);
        return this;
    }
}
