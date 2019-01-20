package com.rcircle.service.auth.util;

import java.util.Calendar;
import java.util.TimeZone;

public class SimpleDate {
    private static final TimeZone gZone = TimeZone.getTimeZone("GMT-0:00");

    public static long getCurrentTime() {
        Calendar cal = java.util.Calendar.getInstance(gZone);
        return cal.getTimeInMillis();
    }

    public static String getCurrentDate() {
        Calendar cal = java.util.Calendar.getInstance(gZone);
        return cal.getTime().toString();
    }
}
