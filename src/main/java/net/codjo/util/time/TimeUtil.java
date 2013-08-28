package net.codjo.util.time;
import java.util.Calendar;
import java.util.TimeZone;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TimeUtil {
    private TimeUtil() {
    }


    /**
     * Converts the given number of milliseconds to a {@link Calendar} whose {@link TimeZone} is GMT.
     *
     * @param milliseconds The number of milliseconds elapsed since midnight, January 1, 1970 UTC, as returned by {@link
     *                     System#currentTimeMillis()}.
     */
    public static Calendar toGMTCalendar(long milliseconds) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(milliseconds);
        return calendar;
    }


    /**
     * Gets the begin of the day represented by <code>milliseconds</code>.
     *
     * @param milliseconds The number of milliseconds elapsed since midnight, January 1, 1970 UTC, as returned by {@link
     *                     System#currentTimeMillis()}.
     *
     * @return A number of milliseconds representing the begin of the day given by the <code>milliseconds</code>
     *         parameter.
     */
    public static long toBeginOfTheDay(long milliseconds) {
        Calendar calendar = toGMTCalendar(milliseconds);
        setToZero(calendar, Calendar.HOUR_OF_DAY);
        setToZero(calendar, Calendar.MINUTE);
        setToZero(calendar, Calendar.SECOND);
        setToZero(calendar, Calendar.MILLISECOND);
        return calendar.getTimeInMillis();
    }


    private static void setToZero(Calendar calendar, int field) {
        calendar.set(field, calendar.getActualMinimum(field));
    }


    /**
     * Converts the triplet (<code>day</code>, <code>hours</code>, <code>minutes</code>) to a number of milliseconds.
     *
     * @param days    The number of days.
     * @param hours   The number of hours.
     * @param minutes The number of minutes.
     *
     * @return The number of milliseconds elapsed since midnight, January 1, 1970 UTC,
     */
    public static long toMilliseconds(int days, int hours, int minutes) {
        return SECONDS.toMillis(((days * 24 * 3600) + (hours * 3600) + (minutes * 60)));
    }
}
