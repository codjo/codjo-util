package net.codjo.util.time;
import junit.framework.TestCase;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
/**
 *
 */
@RunWith(Theories.class)
public class TimeUtilTest extends TestCase {
    private static final long FEW_MINUTES = 45 * 60 * 1000;
    private static final long ONE_DAY = 24 * 3600 * 1000;

    @DataPoint
    public static final Instant MINIMUM = new Instant(0, 0, 0, 0, 0);
    @DataPoint
    public static final Instant DAY0_MIDAY = new Instant(0, 12, 0, ONE_DAY / 2, 0);
    @DataPoint
    public static final Instant DAY0_FEW_MINUTES = new Instant(0, 0, 45, FEW_MINUTES, 0);
    @DataPoint
    public static final Instant DAY0_SOMETIME = new Instant(0,
                                                            DAY0_MIDAY.hours,
                                                            DAY0_FEW_MINUTES.minutes,
                                                            DAY0_MIDAY.expectedMilliseconds
                                                            + DAY0_FEW_MINUTES.expectedMilliseconds,
                                                            0);

    @DataPoint
    public static final Instant DAY1_MIDAY = new Instant(DAY0_MIDAY, 1, ONE_DAY);
    @DataPoint
    public static final Instant DAY1_FEW_MINUTES = new Instant(DAY0_FEW_MINUTES, 1, ONE_DAY);
    @DataPoint
    public static final Instant DAY1_SOMETIME = new Instant(DAY0_SOMETIME, 1, ONE_DAY);


    @Theory
    public void testToGMTCalendar(Instant instant) throws Exception {
        long actualMilliseconds = TimeUtil.toGMTCalendar(instant.expectedMilliseconds).getTimeInMillis();
        assertEquals(instant.expectedMilliseconds, actualMilliseconds);
    }


    @Theory
    public void testToMilliseconds(Instant instant) throws Exception {
        long actualMilliseconds = TimeUtil.toMilliseconds(instant.day, instant.hours, instant.minutes);
        assertEquals(instant.expectedMilliseconds, actualMilliseconds);
    }


    @Theory
    public void testToBeginOfTheDay(Instant instant) {
        long milliseconds = instant.expectedMilliseconds;
        assertEquals(instant.expectedBeginOfTheDay, TimeUtil.toBeginOfTheDay(milliseconds));
    }


    private static class Instant {
        private final int day;
        private final int hours;
        private final int minutes;
        private final long expectedMilliseconds;
        private final long expectedBeginOfTheDay;


        public Instant(Instant instant, int deltaDays, long expectedBeginOfTheDay) {
            this(instant.day + deltaDays, instant.hours, instant.minutes, instant.expectedMilliseconds +
                                                                          ONE_DAY * deltaDays, expectedBeginOfTheDay);
        }


        public Instant(int day, int hours, int minutes, long expectedMilliseconds, long expectedBeginOfTheDay) {
            this.day = day;
            this.hours = hours;
            this.minutes = minutes;
            this.expectedMilliseconds = expectedMilliseconds;
            this.expectedBeginOfTheDay = expectedBeginOfTheDay;
        }


        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Instant");
            sb.append("{minutes=").append(minutes);
            sb.append(", hours=").append(hours);
            sb.append(", day=").append(day);
            sb.append('}');
            return sb.toString();
        }
    }
}
