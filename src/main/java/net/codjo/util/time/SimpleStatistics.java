package net.codjo.util.time;
/**
 * A simple implementation of {@link Statistics} that computes statistics on times provided by calls to {@link
 * #add(Statistics)} and/or {@link #addTime(long)}.
 */
public class SimpleStatistics implements Statistics {
    private int count;
    private long totalTime;
    private long minTime = -1L;
    private long maxTime = -1L;
    private Double meanTime; // use object wrapper for lazy computation
    private Double meanFrequency; // use object wrapper for lazy computation


    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return count;
    }


    /**
     * {@inheritDoc}
     */
    public long getTotalTime() {
        return totalTime;
    }


    /**
     * {@inheritDoc}
     */
    public long getMinTime() {
        return minTime;
    }


    /**
     * {@inheritDoc}
     */
    public long getMaxTime() {
        return maxTime;
    }


    /**
     * {@inheritDoc}
     */
    public double getMeanTime() {
        if (meanTime == null) {
            meanTime = (count == 0) ? 0d : (double)totalTime / count;
        }
        return meanTime;
    }


    /**
     * {@inheritDoc}
     */
    public double getMeanFrequency() {
        if (meanFrequency == null) {
            meanFrequency = (count == 0) ? 0d : 1000 / getMeanTime();
        }
        return meanFrequency;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return (minTime < 0) && (maxTime < 0) && (count == 0);
    }


    /**
     * Adds other statistics to this.
     *
     * @param other Other statistics to add.
     */
    public void add(Statistics other) {
        totalTime += other.getTotalTime();
        count += other.getCount();
        minTime = min(minTime, other.getMinTime());
        maxTime = max(maxTime, other.getMaxTime());

        // these values will be lazily computed later
        meanTime = null;
        meanFrequency = null;
    }


    /**
     * Adds a time to this.
     *
     * @param timeToAdd A number of milliseconds to add.
     */
    public void addTime(long timeToAdd) {
        count++;
        totalTime += timeToAdd;
        minTime = min(minTime, timeToAdd);
        maxTime = max(maxTime, timeToAdd);

        // these values will be lazily computed later
        meanTime = null;
        meanFrequency = null;
    }


    /**
     * Returns the smaller of 2 values, by excluding negative ones.
     */
    public static long min(long value1, long value2) {
        if (value1 < 0) {
            return value2;
        }
        else if (value2 < 0) {
            return value1;
        }
        else {
            return Math.min(value1, value2);
        }
    }


    /**
     * Returns the greater of 2 values, by excluding negative ones.
     */
    public static long max(long value1, long value2) {
        if (value1 < 0) {
            return value2;
        }
        else if (value2 < 0) {
            return value1;
        }
        else {
            return Math.max(value1, value2);
        }
    }


    /**
     * Aggregates a collection of {@link SimpleStatistics}.
     *
     * @return An aggregation of the given statistics.
     */
    public static SimpleStatistics aggregate(Iterable<? extends StatisticsOwner> statOwners) {
        SimpleStatistics result = new SimpleStatistics();
        if (statOwners != null) {
            for (StatisticsOwner statOwner : statOwners) {
                result.add(statOwner.getStatistics());
            }
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(count);
        if (totalTime != 0) {
            builder.append(" - ");
            appendSeconds(builder, "totalTime", totalTime);
            appendSeconds(builder, " minTime", minTime);
            appendSeconds(builder, " maxTime", maxTime);
            appendSeconds(builder, " meanTime", getMeanTime());
            builder.append(" meanFrequency=").append(getMeanFrequency());
        }
        return builder.toString();
    }


    private static void appendSeconds(StringBuilder builder, String name, Number value) {
        builder.append(name).append("=").append(value.doubleValue() / 1000.0).append("s");
    }
}
