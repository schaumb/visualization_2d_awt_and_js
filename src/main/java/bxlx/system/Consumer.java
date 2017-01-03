package bxlx.system;

/**
 * Created by qqcs on 2017.01.03..
 */
public interface Consumer<T> {
    void accept(T data);
}
