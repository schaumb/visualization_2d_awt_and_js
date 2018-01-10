package bxlx.system;

import bxlx.system.functional.MySupplier;

import java.util.List;
import java.util.function.Supplier;

public class ObservableValue<T> extends Observable<T> implements MySupplier<T> {
    private T value;

    public ObservableValue() {

    }

    public ObservableValue(T value) {
        this.value = value;
    }

    public ObservableValue(Supplier<T> get, List<ObservableValue<?>> dependencies) {
        this(get.get());

        for (ObservableValue<?> observableValue : dependencies) {
            observableValue.addObserver((observable, from) -> this.setValue(get.get()));
        }
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
