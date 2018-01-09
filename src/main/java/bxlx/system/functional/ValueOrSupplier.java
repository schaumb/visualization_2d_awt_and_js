package bxlx.system.functional;

import bxlx.system.SystemSpecific;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ValueOrSupplier<T> {
    private Supplier<T> supplier;
    private T lastGetElem;
    private T lastShowedElem;

    public ValueOrSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public ValueOrSupplier(T elem) {
        this.lastGetElem = elem;
    }

    protected ValueOrSupplier(ValueOrSupplier<?> other, Function<Object, T> trans) {
        this.supplier = () -> other.isChanged() ? trans.apply(other.get()) : lastGetElem;
    }

    private ValueOrSupplier(ValueOrSupplier<?> other1, ValueOrSupplier<?> other2, BiFunction<Object, Object, T> trans) {
        this.supplier = () -> other1.isChanged() || other2.isChanged() ? trans.apply(other1.get(), other2.get()) : lastGetElem;
    }

    @SuppressWarnings("unchecked")
    public <U> ValueOrSupplier<U> transform(Function<T, U> trans) {
        return new ValueOrSupplier<>(this, o -> trans.apply((T) o));
    }

    @SuppressWarnings("unchecked")
    public <U, V> ValueOrSupplier<U> transform2(ValueOrSupplier<V> other, BiFunction<T, V, U> trans) {
        return new ValueOrSupplier<>(this, other, (t, v) -> trans.apply((T) t, (V) v));
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
        return !SystemSpecific.get().isEquals(lastShowedElem, supplier == null ? lastGetElem : (lastGetElem = supplier.get()));
    }

    public void commit() {
        lastShowedElem = lastGetElem;
    }

    public Supplier<T> getAsSupplier() {
        return () -> get();
    }

    public T get() {
        return supplier == null ? lastGetElem : (lastGetElem = supplier.get());
    }
}