package net.codjo.util.time;
/**
 * Represents a class that owns a {@link Statistics}.
 */
public interface StatisticsOwner<T extends Statistics> {
    /**
     * @return The {@link Statistics} owned by this instance.
     */
    T getStatistics();
}
