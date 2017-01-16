package bxlx.system;

import java.util.function.Function;
import java.util.function.Supplier;

public class ValueOrSupplier<T> {
    private Supplier<T> supplier;
    private T lastGetElem;
    private T elem;

    public ValueOrSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public ValueOrSupplier(T elem) {
        this.lastGetElem = elem;
    }

    public ValueOrSupplier<T> setSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
        return this;
    }

    public ValueOrSupplier<T> setElem(T elem) {
        this.lastGetElem = elem;
        this.supplier = null;
        return this;
    }

    public boolean isChanged() {
        return !SystemSpecific.get().equals(elem, supplier == null ? lastGetElem : (lastGetElem = supplier.get()));
    }

    public void commit() {
        elem = lastGetElem;
    }

    public Supplier<T> getAsSupplier() {
        return () -> get();
    }

    public T get() {
        return supplier == null ? lastGetElem : (lastGetElem = supplier.get());
    }

    public Supplier<T> transform(Function<T, T> trans) {
        return new ValueOrSupplier<T>((T) null) {
            private T lastGet = ValueOrSupplier.this.get();
            private T tmpGet;
            private T cache = trans.apply(lastGet);

            @Override
            public T get() {
                return !SystemSpecific.get().equals(lastGet, tmpGet = ValueOrSupplier.this.get()) ?
                        cache = trans.apply(lastGet = tmpGet) : cache;
            }
        }.getAsSupplier();
    }
}