package com.gas.common.ui.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /*
     * @param month is 0-based
     */
    public static Date create(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date create(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public static Date create(String year, String month, String date) {
        return create(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));
    }

    public static boolean isSameDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(d1.getTime());
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(d2.getTime());
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }
}
