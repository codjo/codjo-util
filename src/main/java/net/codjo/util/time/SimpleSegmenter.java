package net.codjo.util.time;
import net.codjo.util.time.SegmentedStatistics.Segmenter;
/**
 * A simple implementation of {@link Segmenter} that divide time into segments of a fixed size.
 */
public class SimpleSegmenter implements Segmenter {
    protected final long segmentSize;


    /**
     * @param segmentSize The size of a segment.
     *
     * @throws IllegalArgumentException if <code>segmentSize</code><= 0
     */
    public SimpleSegmenter(long segmentSize) {
        if (segmentSize <= 0) {
            throw new IllegalArgumentException("segmentSize must be strictly positive");
        }
        this.segmentSize = segmentSize;
    }


    /**
     * {@inheritDoc}
     */
    public int getSegment(long begin, long duration) {
        return (int)(begin / segmentSize);
    }


    /**
     * {@inheritDoc}
     */
    public String getSegmentName(long segment) {
        return String.valueOf(segment);
    }


    /**
     * @return The size of a segment.
     */
    public long getSegmentSize() {
        return segmentSize;
    }
}
