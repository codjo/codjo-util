package net.codjo.util.time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * A specialized implementation of {@link SimpleSegmenter} that divides a day into segment of equal values. If times are
 * added across multiple days, they are aggregated as if they were all in the same day. Example :<br/> <code>
 * DaySegmenter segmenter = new DaySegmenter("HH:mm", 24); // segments of one hour int segment0 =
 * getSegment(toMilliseconds(0, 1, 0); // day 0, 1 o'clock int segment1 = getSegment(toMilliseconds(1, 1, 0); // day 1,
 * 1 o'clock assert segment0==segment1; // is true </code>
 */
public class DaySegmenter extends SimpleSegmenter {
    private static final long ONE_DAY = MILLISECONDS.convert(24L * 3600L, SECONDS);
    private final String segmentNameFormat;


    /**
     * @param segmentNameFormat The format of the bound (begin or end) of the segment. It must comply with the
     *                          specification of {@link java.text.SimpleDateFormat}.
     */
    public DaySegmenter(String segmentNameFormat, int numberOfSegmentsPerDay) {
        super(segmentSize(numberOfSegmentsPerDay));
        this.segmentNameFormat = segmentNameFormat;
    }


    private static long segmentSize(long numberOfSegmentsPerDay) {
        if (numberOfSegmentsPerDay <= 0) {
            return throwIllegalArgumentException(numberOfSegmentsPerDay, "strictly positive");
        }
        if ((ONE_DAY % numberOfSegmentsPerDay) != 0) {
            return throwIllegalArgumentException(numberOfSegmentsPerDay,
                                                 "a divider of a day expressed in milliseconds");
        }
        return ONE_DAY / numberOfSegmentsPerDay;
    }


    private static long throwIllegalArgumentException(long numberOfSegmentsPerDay, String constraint) {
        throw new IllegalArgumentException(
              "numberOfSegmentsPerDay(" + numberOfSegmentsPerDay + ") must be " + constraint);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getSegment(long begin, long duration) {
        begin -= TimeUtil.toBeginOfTheDay(begin);

        return super.getSegment(begin, duration);
    }


    /**
     * Gets the string representation of a segment. The general pattern is <i>B</i>-<i>E</i>, where <i>B</i> and
     * <i>E</i> are respectively the begin and the end of the segment formatted as specified by {#segmentNameFormat}.
     * Example :<br/> <code> String segmentName = new DaySegmenter("HH:mm", 24).getSegmentName(2); assert
     * "02:00".equals(segmentName); // is true </code>
     *
     * @param segment The identifier of the segment.
     *
     * @return A string representing the segment.
     */
    @Override
    public String getSegmentName(long segment) {
        SimpleDateFormat format = new SimpleDateFormat(segmentNameFormat);
        Calendar calendar = TimeUtil.toGMTCalendar(segmentSize * segment);
        format.setCalendar(calendar);
        StringBuilder result = new StringBuilder(format.format(calendar.getTime()));
        result.append('-');

        calendar.add(Calendar.MILLISECOND, (int)segmentSize);

        result.append(format.format(calendar.getTime()));

        return result.toString();
    }
}
