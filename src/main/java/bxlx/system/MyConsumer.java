package bxlx.system;

import java.util.function.Consumer;

/**
 * Created by qqcs on 2017.01.03..
 */
public interface MyConsumer<T> {
    void accept(T data);

    default Consumer<T> getAsConsumer() {
        return d -> accept(d);
    }
}
