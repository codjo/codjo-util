package net.codjo.util.time;
import java.util.logging.Logger;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.codjo.util.time.TimeUtil.toMilliseconds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
/**
 *
 */
public class DaySegmenterTest extends AbstractSegmenterTest {
    private static Logger LOG = Logger.getLogger(DaySegmenterTest.class.getName());

    private static final int NB_SEGMENTS_PER_DAY = 24;

    private static final long MINUTE = MILLISECONDS.convert(60, SECONDS);
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    @DataPoint
    public static final TestData INTO_SEGMENT_1_BEGIN = new TestData(NB_SEGMENTS_PER_DAY, toMilliseconds(0, 1, 0), 3, 1,
                                                                     "01:00-02:00");
    @DataPoint
    public static final TestData INTO_SEGMENT_1_END = new TestData(NB_SEGMENTS_PER_DAY, toMilliseconds(0, 1, 59), 1, 1,
                                                                   "01:00-02:00");
    @DataPoint
    public static final TestData INTO_SEGMENT_14 = new TestData(NB_SEGMENTS_PER_DAY, toMilliseconds(0, 14, 23), 3, 14,
                                                                "14:00-15:00");
    @DataPoint
    public static final TestData ACROSS_SEGMENT_19_AND_20 = new TestData(NB_SEGMENTS_PER_DAY,
                                                                         toMilliseconds(0, 19, 25),
                                                                         HOUR,
                                                                         19,
                                                                         "19:00-20:00");
    @DataPoint
    public static final TestData DAY_3 = new TestData(NB_SEGMENTS_PER_DAY, toMilliseconds(3, 1, 42), 9, 1,
                                                      "01:00-02:00");

    @DataPoint
    public static final int ZERO_SEGMENTS = (int)((DAY - 1) / 2);
    @DataPoint
    public static final int _13_SEGMENTS = (int)((DAY - 1) / 13);
    @DataPoint
    public static final int _20_SEGMENTS = (int)((DAY - 1) / 20);
    @DataPoint
    public static final int _99_SEGMENTS = (int)((DAY - 1) / 99);


    @Theory
    public void testGetSegmentSize(TestData data) {
        long nbSegmentsPerDay = data.segmentSize;
        SimpleSegmenter segmenter = createSegmenter(nbSegmentsPerDay);

        long expectedSegmentSize = DAY / nbSegmentsPerDay;
        assertEquals(expectedSegmentSize, segmenter.getSegmentSize());
    }


    @Theory
    public void testWrongNumberOfSegments(int nbSegmentsPerDay) {
        try {
            createSegmenter(nbSegmentsPerDay);
            fail("IllegalArgumentException is expected");
        }
        catch (IllegalArgumentException iae) {
            // ok
        }
    }


    @Override
    protected SimpleSegmenter createSegmenter(long nbSegmentsPerDay) {
        return new DaySegmenter("HH:mm", (int)nbSegmentsPerDay);
    }
}
