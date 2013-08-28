package net.codjo.util.time;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
/**
 * An implementation of {@link Statistics} that use a {@link Segmenter} to divide time into segments.
 */
public class SegmentedStatistics implements Statistics {
    /**
     * The overall statistics represented by this.
     */
    private final SimpleStatistics globalStatistics = new SimpleStatistics();

    /**
     * The statistics for each segment containg at least one value.
     */
    private final Map<Integer, SimpleStatistics> statsPerSegment = new HashMap<Integer, SimpleStatistics>();

    /**
     * An interface used to define how time is divided into segments.
     */
    public static interface Segmenter {
        /**
         * Gets the identifier of the segment associated with the interval of time represented by (<code>begin</code>,
         * <code>duration</code>). Note : In case the period cross multiple segments, it's up to the implementer to
         * define if the begin or the end of the period should be used.
         *
         * @param begin    The begin of the period (usually in milliseconds since midnight, January 1, 1970 UTC).
         * @param duration The duration of the period.
         *
         * @return The identifier of the segment associated with the given period of time.
         */
        int getSegment(long begin, long duration);


        /**
         * Gets the name of a segment given by its number.
         *
         * @param segment The number of a segment.
         *
         * @return The name of a segment given by its number.
         */
        String getSegmentName(long segment);
    }

    private final Segmenter segmenter;


    /**
     * @param segmenter The Segmenter to use.
     */
    public SegmentedStatistics(Segmenter segmenter) {
        this.segmenter = segmenter;
    }


    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return globalStatistics.getCount();
    }


    /**
     * {@inheritDoc}
     */
    public long getTotalTime() {
        return globalStatistics.getTotalTime();
    }


    /**
     * {@inheritDoc}
     */
    public long getMinTime() {
        return globalStatistics.getMinTime();
    }


    /**
     * {@inheritDoc}
     */
    public long getMaxTime() {
        return globalStatistics.getMaxTime();
    }


    /**
     * {@inheritDoc}
     */
    public double getMeanTime() {
        return globalStatistics.getMeanTime();
    }


    /**
     * {@inheritDoc}
     */
    public double getMeanFrequency() {
        return globalStatistics.getMeanFrequency();
    }


    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return globalStatistics.isEmpty();
    }


    /**
     * Add the given <code>duration</code> to the statistics for segment associated with the time given by
     * <code>begin</code>.
     */
    public void addTime(long begin, long duration) {
        globalStatistics.addTime(duration);

        int segment = segmenter.getSegment(begin, duration);
        SimpleStatistics stats = statsPerSegment.get(segment);
        if (stats == null) {
            stats = new SimpleStatistics();
            statsPerSegment.put(segment, stats);
        }
        stats.addTime(duration);
    }


    /**
     * Get the sorted set of identifiers for segments containing at least one value.
     */
    public SortedSet<Integer> getSegments() {
        return new TreeSet<Integer>(statsPerSegment.keySet());
    }


    /**
     * Gets the statistics for a segment given by its identifier.
     *
     * @param segmentId The segment identifier.
     */
    public Statistics getSegmentStatistics(int segmentId) {
        Statistics result = statsPerSegment.get(segmentId);
        return (result == null) ? Statistics.NONE : result;
    }


    /**
     * Gets the overall overall statistics represented by this.
     */
    public SimpleStatistics getGlobalStatistics() {
        return globalStatistics;
    }
}
