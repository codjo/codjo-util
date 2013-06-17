package net.codjo.util.time;
/**
 * Default implementation of {@link TimeSource} that calls {@link System#currentTimeMillis()}.
 */
public final class SystemTimeSource implements TimeSource {
    static final SystemTimeSource INSTANCE = new SystemTimeSource();


    public static TimeSource defaultIfNull(TimeSource timeSource) {
        return (timeSource == null) ? INSTANCE : timeSource;
    }


    private SystemTimeSource() {
    }


    public long getTime() {
        return System.currentTimeMillis();
    }
}
