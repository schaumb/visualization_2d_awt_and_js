package bxlx.system;

/**
 * Created by qqcs on 2017.01.02..
 */
public class Timer {
    private long start;
    private long length;

    public Timer() {
        this(0);
    }

    public Timer(int length) {
        start = SystemSpecific.get().getTime();
        this.length = length;
    }

    public Timer setStart() {
        this.start = SystemSpecific.get().getTime();
        return this;
    }

    public Timer setLength(long length) {
        this.length = length;
        return this;
    }

    public long elapsedTime() {
        return SystemSpecific.get().getTime() - start;
    }

    public boolean elapsed() {
        return need() <= 0;
    }

    public long need() {
        return length - elapsedTime();
    }

    public long length() {
        return length;
    }

    public double percent() {
        return length > 0 ? Math.max(0, Math.min(1, elapsedTime() * 1.0 / length)) : 1.0;
    }
}
