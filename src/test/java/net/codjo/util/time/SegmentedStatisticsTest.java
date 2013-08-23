package net.codjo.util.time;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
/**
 *
 */
public class SegmentedStatisticsTest extends AbstractStatisticsTest<SegmentedStatistics> {
    private static Logger LOG = Logger.getLogger(SegmentedStatisticsTest.class.getName());

    private static final long SEGMENT_SIZE = 10;

    @DataPoint
    public static final SegmentedTestData ONE_SEGMENT = new SegmentedTestData(TWO_VALUES);
    @DataPoint
    public static final SegmentedTestData TWO_SEGMENTS = new SegmentedTestData(TWO_VALUES, THREE_VALUES);
    @DataPoint
    public static final SegmentedTestData THREE_SEGMENTS = new SegmentedTestData(TWO_VALUES, ONE_VALUE, THREE_VALUES);


    @Test
    public void testCrossSegment() {
        final long segmentSize = 20;
        SegmentedStatistics actualStats = createStatistics(segmentSize);

        final long begin0 = 12;
        final long duration0 = 16;
        actualStats.addTime(begin0, duration0);

        final long begin2 = 40;
        final long duration2 = 2;
        actualStats.addTime(begin2, duration2);

        SortedSet<Integer> actualSegments = actualStats.getSegments();
        assertEquals("expected number of segments", 2, actualSegments.size());

        int segment0 = 0;
        int segment2 = 2;
        assertArrayEquals("expected segments",
                          new Integer[]{segment0, segment2},
                          actualSegments.toArray(new Integer[actualSegments.size()]));

        assertSegmentStatisticsEquals(1, duration0, duration0, duration0, duration0,
                                      1000d / duration0, actualStats, segment0);
        assertSegmentStatisticsEquals(1, duration2, duration2, duration2, duration2,
                                      1000d / duration2, actualStats, segment2);
    }


    @Theory
    public void testGetSegments(SegmentedTestData data) {
        SegmentedStatistics actualStats = createOneQuery(data);

        SortedSet<Integer> actualSegments = actualStats.getSegments();
        assertNotNull(actualSegments);
        assertEquals("expected number of segments", data.segments.length, actualSegments.size());

        Integer[] expectedSegments = getExpectedSegments(data);
        assertArrayEquals("expected segments",
                          expectedSegments,
                          actualSegments.toArray(new Integer[actualSegments.size()]));
    }


    @Theory
    public void testGetSegmentStatistics(SegmentedTestData data) {
        SegmentedStatistics globalStats = createOneQuery(data);

        for (int segment : getExpectedSegments(data)) {
            String segmentName = "segment" + segment;
            TestData expectedStats = data.segments[segment];
            Statistics actualStats = globalStats.getSegmentStatistics(segment);
            assertStatisticsEquals(segmentName, expectedStats, actualStats);
        }
    }


    private Integer[] getExpectedSegments(SegmentedTestData data) {
        Integer[] expectedSegments = new Integer[data.segments.length];
        for (int i = 0; i < expectedSegments.length; i++) {
            expectedSegments[i] = i;
        }

        LOG.info("expectedSegments=" + Arrays.asList(expectedSegments));
        return expectedSegments;
    }


    @Theory
    public void testGetGlobalStatistics(SegmentedTestData data) {
        SegmentedStatistics actualStats = createOneQuery(data);

        assertStatisticsEquals("global", data, actualStats);
    }


    private void assertStatisticsEquals(String segmentName, TestData expectedStats, Statistics actualStats) {
        assertStatisticsEquals(segmentName, expectedStats.times.length, expectedStats.expectedTotalTime,
                               expectedStats.expectedMinTime, expectedStats.expectedMaxTime,
                               expectedStats.expectedMeanTime, expectedStats.expectedMeanFrequency,
                               actualStats);
    }


    private void assertSegmentStatisticsEquals(int count, long total, long min, long max,
                                               double meanTime,
                                               double meanFreq,
                                               SegmentedStatistics stats,
                                               int segment) {
        assertStatisticsEquals("segment" + segment, count, total, min, max,
                               meanTime, meanFreq, stats.getSegmentStatistics(segment));
    }


    private void assertStatisticsEquals(String segmentName, int count, long total, long min, long max,
                                        double meanTime, double meanFreq, Statistics actualStats) {
        assertNotNull(actualStats);
        assertEquals(segmentName + ".empty", count == 0, actualStats.isEmpty());
        assertEquals(segmentName + ".count", count, actualStats.getCount());
        assertEquals(segmentName + ".totalTime", total, actualStats.getTotalTime());
        assertEquals(segmentName + ".minTime", min, actualStats.getMinTime());
        assertEquals(segmentName + ".maxTime", max, actualStats.getMaxTime());
        assertEquals(segmentName + ".meanTime", meanTime, actualStats.getMeanTime());
        assertEquals(segmentName + ".meanFrequency", meanFreq, actualStats.getMeanFrequency());
    }


    private static class SegmentedTestData extends TestData {
        private final TestData[] segments;


        public SegmentedTestData(TestData... segments) {
            super(min(segments), max(segments), total(segments), times(segments));
            this.segments = segments;
        }


        private static long total(TestData[] segments) {
            long result = 0L;
            for (TestData segment : segments) {
                result += segment.expectedTotalTime;
            }
            return result;
        }


        private static long max(TestData[] segments) {
            long result = -1L;
            for (TestData segment : segments) {
                result = SimpleStatistics.max(result, segment.expectedMaxTime);
            }
            return result;
        }


        private static long min(TestData[] segments) {
            long result = -1L;
            for (TestData segment : segments) {
                result = SimpleStatistics.min(result, segment.expectedMinTime);
            }
            return result;
        }


        private static long[] times(TestData[] segments) {
            int size = 0;
            for (TestData segment : segments) {
                size += segment.times.length;
            }

            long[] result = new long[size];
            int index = 0;
            for (TestData segment : segments) {
                for (long time : segment.times) {
                    result[index] = time;
                    index++;
                }
            }
            return result;
        }
    }


    @Override
    SegmentedStatistics createStatistics() {
        return createStatistics(SEGMENT_SIZE);
    }


    SegmentedStatistics createStatistics(long segmentSize) {
        return new SegmentedStatistics(new SimpleSegmenter(segmentSize));
    }


    @Override
    void addTime(TestData currentData, int currentDataIndex, SegmentedStatistics actualStats, long duration) {
        long currentTime = 0L;

        if (currentData instanceof SegmentedTestData) {
            SegmentedTestData data = (SegmentedTestData)currentData;

            // find current time
            int segmentBeginIndex = 0;
            int currentSegment = -1;
            do {
                currentSegment++;
                segmentBeginIndex += data.segments[currentSegment].times.length;
            }
            while (segmentBeginIndex <= currentDataIndex);
            currentTime = SEGMENT_SIZE * currentSegment;
        }

        actualStats.addTime(currentTime, duration);
    }
}
