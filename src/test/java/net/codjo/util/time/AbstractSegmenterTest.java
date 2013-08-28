package net.codjo.util.time;
import net.codjo.util.time.SegmentedStatistics.Segmenter;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
/**
 *
 */
@RunWith(Theories.class)
abstract public class AbstractSegmenterTest {
    @Theory
    public void testGetSegment(TestData data) {
        Segmenter segmenter = createSegmenter(data.segmentSize);

        int actualSegment = segmenter.getSegment(data.begin, data.duration);

        assertEquals("segment", data.expectedSegment, actualSegment);
    }


    @Theory
    public void testGetSegmentName(TestData data) throws Exception {
        Segmenter segmenter = createSegmenter(data.segmentSize);

        int segment = segmenter.getSegment(data.begin, data.duration);
        String actualSegmentName = segmenter.getSegmentName(segment);

        assertEquals("segmentName", data.expectedSegmentName, actualSegmentName);
    }


    abstract protected SimpleSegmenter createSegmenter(long segmentSize);


    protected static class TestData {
        protected final long segmentSize;
        protected final long begin;
        protected final long duration;
        private final int expectedSegment;
        private final String expectedSegmentName;


        public TestData(long segmentSize, long begin, long duration, int expectedSegment, String expectedSegmentName) {
            this.segmentSize = segmentSize;
            this.begin = begin;
            this.duration = duration;
            this.expectedSegment = expectedSegment;
            this.expectedSegmentName = expectedSegmentName;
        }
    }
}
