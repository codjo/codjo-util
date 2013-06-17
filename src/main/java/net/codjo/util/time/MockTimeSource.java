package net.codjo.util.time;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of {@link TimeSource} that can be used for testing.
 */
public class MockTimeSource implements TimeSource {
    private final AtomicLong time = new AtomicLong();

    /**
     * If >= 1, <code>time</code> will be incremented automatically with that value at each call to {@link #getTime()}.
     */
    private long autoIncrement;


    public MockTimeSource() {
        this(0L);
    }


    /**
     * @param autoIncrement See {@link #autoIncrement}.
     */
    public MockTimeSource(long autoIncrement) {
        this.autoIncrement = autoIncrement;
    }


    /**
     * Get the current value of this time source.
     */
    public long getTime() {
        return (autoIncrement > 0) ? time.getAndAdd(autoIncrement) : time.longValue();
    }


    /**
     * Add 1 to this time source.
     */
    public void inc() {
        time.incrementAndGet();
    }


    /**
     * Add <code>increment</code> to this time source.
     */
    public void inc(long increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("increment (" + increment + ") < 0");
        }
        time.addAndGet(increment);
    }


    /**
     * Set this time source to 0.
     */
    public void reset() {
        time.set(0);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MockTimeSource");
        sb.append("{time=").append(time);
        sb.append(", autoIncrement=").append(autoIncrement);
        sb.append('}');
        return sb.toString();
    }


    /**
     * Set the {@link #autoIncrement} of this time source.
     *
     * @param autoIncrement See {@link #autoIncrement}.
     */
    public void setAutoIncrement(long autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
