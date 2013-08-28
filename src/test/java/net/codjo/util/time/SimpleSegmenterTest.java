package net.codjo.util.time;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;

import static org.junit.Assert.assertEquals;
/**
 *
 */
public class SimpleSegmenterTest extends AbstractSegmenterTest {
    @DataPoint
    public static TestData INTO_SEGMENT_1_BEGIN = new TestData(5, 5, 3, 1, "1");
    @DataPoint
    public static TestData INTO_SEGMENT_1_END = new TestData(5, 9, 1, 1, "1");
    @DataPoint
    public static TestData INTO_SEGMENT_2 = new TestData(5, 10, 3, 2, "2");
    @DataPoint
    public static TestData ACROSS_SEGMENT_1_AND_2 = new TestData(5, 6, 7, 1, "1");


    @Override
    protected SimpleSegmenter createSegmenter(long segmentSize) {
        return new SimpleSegmenter(segmentSize);
    }


    @Theory
    public void testGetSegmentSize(TestData data) {
        SimpleSegmenter segmenter = createSegmenter(data.segmentSize);

        assertEquals(data.segmentSize, segmenter.getSegmentSize());
    }
}
