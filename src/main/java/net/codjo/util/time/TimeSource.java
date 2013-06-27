package net.codjo.util.time;
/**
 * Interface used to get the current time, as specified by {@link System#currentTimeMillis()}. The main interest of this
 * class is to be able to test time dependant features in a deterministic way. Note that's more or less equivalent to
 * Ticker class in guava library.
 */
public interface TimeSource {
    /**
     * @return The current time.
     */
    long getTime();
}
