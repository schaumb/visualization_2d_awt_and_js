package bxlx.system;

import bxlx.system.functional.MySupplier;

import java.util.function.Function;

public class ObservableValue<T> extends Observable<T> implements MySupplier<T> {
    private T value;

    public ObservableValue() {

    }

    public ObservableValue(T value) {
        this.value = value;
    }

    public ObservableValue(ObservableValue<?> other, Function<Object, T> trans) {
        this(trans.apply(other.get()));

        other.addObserver((observable, from) -> this.setValue(trans.apply(from)));
    }

    public T get() {
        return value;
    }

    public ObservableValue<T> setValue(T value) {
        if (!SystemSpecific.get().isEquals(this.value, value)) {
            this.value = value;
            notifyObservers(value);
        }

        return this;
    }
}
