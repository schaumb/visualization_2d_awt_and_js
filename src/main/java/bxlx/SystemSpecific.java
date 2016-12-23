package bxlx;

/**
 * Created by qqcs on 2016.12.23..
 */
public abstract class SystemSpecific {
    private static SystemSpecific INSTANCE;

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

    abstract public boolean isEqual(double d1, double d2);
    abstract public long getTime();
    abstract public void log(String message);
    abstract public void log(CommonError commonError, String message);
}
