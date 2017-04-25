package bxlx.system.input;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by ecosim on 4/25/17.
 */
public class ButtonWithData<T extends Button.Clickable, D> extends Button<T> {
    private final D data;
    public ButtonWithData(T clickable, Consumer<Button<T>> atClick, Consumer<Button<T>> atHold, Supplier<Boolean> disabledSuppl, D data) {
        super(clickable, atClick, atHold, disabledSuppl);
        this.data = data;
    }

    public D getData() {
        return data;
    }
}
