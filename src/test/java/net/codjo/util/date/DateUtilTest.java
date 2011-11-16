package net.codjo.util.date;
import java.util.Calendar;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.getInstance;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
/**
 *
 */
public class DateUtilTest {

    @Test
    public void test_getUSDateFromDate() {
        Calendar calendar = getInstance();
        calendar.set(1978, JUNE, 29);
        Date date = calendar.getTime();

        assertEquals("1978-06-29", DateUtil.getUSDate(date));
    }


    @Test
    public void test_getUSDateFromLong() {
        Calendar calendar = getInstance();
        calendar.set(1978, JUNE, 29);
        Date date = calendar.getTime();

        assertEquals("1978-06-29", DateUtil.getUSDate(date.getTime()));
    }


    @Test
    public void test_getUSDateFromString() {
        String date = "1978-06-29 16:10:05.8";
        assertEquals("1978-06-29", DateUtil.getUSDate(date));
    }


    @Test
    public void test_getTimestampDate() {
        Calendar calendar = getInstance();
        calendar.set(1978, JUNE, 29, 16, 10, 5);
        Date date = calendar.getTime();

        assertEquals("1978-06-29 16:10:05", DateUtil.getTimestampDate(date));
    }


    @Test
    public void test_getFrenchDate() {
        Calendar calendar = getInstance();
        calendar.set(1978, JUNE, 29);
        Date date = calendar.getTime();

        assertEquals("29/06/1978", DateUtil.getFrenchDate(date));
    }


    @Test
    public void test_parseUSDate() {
        assertNull(DateUtil.parseUSDate(null));

        assertNull(DateUtil.parseUSDate("null"));

        final Date parsedDate = DateUtil.parseUSDate("1978-06-29");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        assertEquals(29, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
        assertEquals(1978, calendar.get(Calendar.YEAR));
    }


    @Test
    public void test_parseFrenchDate() {
        assertNull(DateUtil.parseFrenchDate(null));

        assertNull(DateUtil.parseFrenchDate("null"));

        final Date parsedDate = DateUtil.parseFrenchDate("29/06/1978");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        assertEquals(29, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
        assertEquals(1978, calendar.get(Calendar.YEAR));
    }


    @Test
    public void test_shiftUSDate() {
        assertEquals("1978-06-24", DateUtil.shiftUSDate("1978-06-29", -5));

        assertEquals("1977-06-29", DateUtil.shiftUSDate("1978-06-29", -365));
    }


    @Test
    public void test_shiftDate() {
        Calendar calendar = getInstance();
        calendar.set(1978, JUNE, 29);
        Date date = calendar.getTime();

        Date shiftedDate = DateUtil.shiftDate(date, -5);
        final Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTime(shiftedDate);
        assertEquals(24, otherCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.JUNE, otherCalendar.get(Calendar.MONTH));
        assertEquals(1978, otherCalendar.get(Calendar.YEAR));
    }


    @Test
    public void test_hasDateChanged() {
        assertFalse(DateUtil.hasDateChanged(null, null));
        assertFalse(DateUtil.hasDateChanged(null, "null"));
        assertTrue(DateUtil.hasDateChanged(null, "1978-06-29"));

        Calendar calendar = getInstance();
        calendar.set(1978, JUNE, 29);
        Date oldDate = calendar.getTime();

        assertTrue(DateUtil.hasDateChanged(oldDate, null));
        assertTrue(DateUtil.hasDateChanged(oldDate, "null"));
        assertTrue(DateUtil.hasDateChanged(oldDate, "2000-01-01"));
        assertFalse(DateUtil.hasDateChanged(oldDate, "1978-06-29"));
    }
}
