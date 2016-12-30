package bxlx;

import bxlx.graphics.ICanvas;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by qqcs on 2016.12.23..
 */
public abstract class SystemSpecific {
    protected static SystemSpecific INSTANCE;

    public static SystemSpecific get() {
        if(INSTANCE == null) {
            throw new CommonError("No singleton instance", "no system specific instance created");
        }
        return INSTANCE;
    }

    protected SystemSpecific() {
        if(INSTANCE != null) {
            throw new CommonError("Singleton two instance", "2 or more system specific instance created");
        }
        INSTANCE = this;
    }

    abstract public void setDrawFunction(MyConsumer<ICanvas> canvasConsumer);
    abstract public boolean isEqual(double d1, double d2);
    abstract public long getTime();
    abstract public void log(String message);
    abstract public void log(CommonError commonError, String message);
    abstract public void playMusic(String src);
}
