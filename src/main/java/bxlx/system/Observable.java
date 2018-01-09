package bxlx.system;

import java.util.ArrayList;

public abstract class Observable<T> {
    private final ArrayList<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(T object) {
        for (Observer<T> observer : observers) {
            observer.update(this, object);
        }
    }
}
