package bxlx.system.functional;

import bxlx.system.SystemSpecific;

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

    public static class Transform<T, U> {
        public Supplier<U> transform(ValueOrSupplier<T> supplier, Function<T, U> trans) {
            final Supplier<T> suppl = supplier.getAsSupplier();
            return new ValueOrSupplier<U>((U) null) {
                private T lastGet = suppl.get();
                private T tmpGet;
                private U cache = trans.apply(lastGet);

                @Override
                public U get() {
                    return !SystemSpecific.get().equals(lastGet, tmpGet = suppl.get()) ?
                            cache = trans.apply(lastGet = tmpGet) : cache;
                }
            }.getAsSupplier();
        }
    }
}