package bxlx.system;

public interface Observer<T> {
    void update(Observable<T> observable, T from);
}
