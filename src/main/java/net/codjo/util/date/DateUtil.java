package net.codjo.util.date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import net.codjo.util.string.StringUtil;
/**
 *
 */
public class DateUtil {
    public static final DateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String US_DATE_PATTERN = "yyyy-MM-dd";
    public static final DateFormat US_FORMATTER = new SimpleDateFormat(US_DATE_PATTERN);
    private static final DateFormat FRENCH_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");


    private DateUtil() {
    }


    public static String getUSCurrentDate() {
        return getUSDate(new Date(System.currentTimeMillis()));
    }


    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }


    public static String getUSDate(Date date) {
        return US_FORMATTER.format(date);
    }


    public static String getUSDate(long millis) {
        return getUSDate(new Date(millis));
    }


    public static String getUSDate(String date) {
        return getUSDate(parseUSDate(date));
    }


    public static String getTimestampDate(Date date) {
        return TIMESTAMP_FORMATTER.format(date);
    }


    public static String getFrenchDate(Date date) {
        return FRENCH_FORMATTER.format(date);
    }


    public static Date parseUSDate(String date) {
        return parseDate(date, US_FORMATTER);
    }


    public static Date parseFrenchDate(String date) {
        return parseDate(date, FRENCH_FORMATTER);
    }


    public static Date parseTimestampDate(String date) {
        return parseDate(date, TIMESTAMP_FORMATTER);
    }


    private static Date parseDate(String date, DateFormat formatter) {
        if (StringUtil.isNull(date)) {
            return null;
        }
        try {
            return formatter.parse(date);
        }
        catch (ParseException exp) {
            throw new RuntimeParseException(exp);
        }
    }


    public static String shiftUSDate(String dateToShift, int nbOfDays) {
        String shiftedDate;
        Date date = parseUSDate(dateToShift);
        shiftedDate = getUSDate(shiftDate(date, nbOfDays));

        return shiftedDate;
    }


    public static Date shiftDate(Date dateToShift, int nbOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateToShift);
        calendar.add(Calendar.DAY_OF_MONTH, nbOfDays);
        return calendar.getTime();
    }


    public static boolean hasDateChanged(Date oldDate, String newDate) {
        boolean hasDateChanged;
        if (oldDate == null) {
            hasDateChanged = StringUtil.isNotNull(newDate);
        }
        else {
            String oldStrDate = getUSDate(oldDate);
            hasDateChanged = StringUtil.isNull(newDate) || !oldStrDate.equals(getUSDate(newDate));
        }
        return hasDateChanged;
    }


    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
