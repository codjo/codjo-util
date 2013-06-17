package net.codjo.util.time;

public class Chronometer {
    private final TimeSource timeSource;
    private long startTime;
    private long endTime;


    public Chronometer() {
        this(null);
    }


    public Chronometer(TimeSource timeSource) {
        this.timeSource = SystemTimeSource.defaultIfNull(timeSource);
    }


    public long getStartTime() {
        return startTime;
    }


    public long getEndTime() {
        return endTime;
    }


    public long getDelay() {
        return endTime - startTime;
    }


    public void start() {
        startTime = timeSource.getTime();
    }


    public void stop() {
        endTime = timeSource.getTime();
    }
}
