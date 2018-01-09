package bxlx.system.functional;

import java.util.function.Supplier;

public interface MySupplier<T> {
    T get();

    default Supplier<T> getAsSupplier() {
        return this::get;
    }
}
