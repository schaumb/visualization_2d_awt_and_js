package bxlx.system;

import bxlx.graphics.Color;
import bxlx.graphics.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by qqcs on 2016.12.23..
 */
public abstract class SystemSpecific {
    protected static SystemSpecific INSTANCE;
    protected boolean rendering = false;
    protected String[] args;
    protected List<IMouseEventListener> listeners = new ArrayList<>();

    public static SystemSpecific get() {
        if (INSTANCE == null) {
            throw new CommonError("No singleton instance", "no system specific instance created");
        }
        return INSTANCE;
    }

    protected SystemSpecific() {
        if (INSTANCE != null) {
            throw new CommonError("Singleton two instance", "2 or more system specific instance created");
        }
        INSTANCE = this;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    abstract public void setDrawFunction(IRenderer renderer);

    public final void setMouseEventListenerQueue(IMouseEventListener listener) {
        listeners.add(listener);
        setMouseEventListeners();
    }

    abstract protected void setMouseEventListeners();

    abstract public boolean isEqual(double d1, double d2);

    abstract public long getTime();

    abstract public void log(String message);

    abstract public void log(CommonError commonError, String message);

    abstract public void playMusic(String src);

    abstract public void readTextFileAsync(String fileName, Consumer<String> consumer);

    abstract public Size preLoad(String src, boolean img);

    abstract public Color getColor(String pic, double x, double y);

    public final boolean isRendering() {
        return rendering;
    }

    abstract public <T> boolean equals(T first, T second);
}
