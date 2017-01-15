package bxlx.system;

import java.util.function.Supplier;

public class ValueOrSupplier<T> {
    private boolean changed = true;
    private Supplier<T> supplier;
    private T elem;

    public ValueOrSupplier(ValueOrSupplier<T> supplier) {
        this.supplier = supplier.getAsSupplier();
    }

    public ValueOrSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public ValueOrSupplier(T elem) {
        this.elem = elem;
    }

    public ValueOrSupplier<T> setSupplier(ValueOrSupplier<T> supplier) {
        return setSupplier(supplier.getAsSupplier());
    }

    public ValueOrSupplier<T> setSupplier(Supplier<T> supplier) {
        if (supplier == null && elem != null) {
            changed = true;
        }
        this.supplier = supplier;
        return this;
    }

    public ValueOrSupplier<T> setElem(T elem) {
        if (supplier != null || this.elem != elem) {
            changed = true;
        }
        this.elem = elem;
        this.supplier = null;
        return this;
    }

    public boolean isChanged() {
        return changed || (supplier != null && !SystemSpecific.get().equals(elem, supplier.get()));
    }

    public void setChangedToFalse() {
        changed = false;
    }

    public Supplier<T> getAsSupplier() {
        return () -> get();
    }

    public T get() {
        return supplier == null ? elem : (elem = supplier.get());
    }
}