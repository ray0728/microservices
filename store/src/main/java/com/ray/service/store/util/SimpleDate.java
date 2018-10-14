package com.ray.service.store.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SimpleDate {
    private static final TimeZone gZone = TimeZone.getTimeZone("GMT-0:00");

    public static long getCurrentTime() {
        Calendar cal = java.util.Calendar.getInstance(gZone);
        return cal.getTimeInMillis();
    }

    public static String getCurrentTimeInMillis(){
        Calendar cal = java.util.Calendar.getInstance(gZone);
        DateFormat dateFormat = new java.text.SimpleDateFormat("hhmmss:SSS");
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDate(){
        Calendar cal = java.util.Calendar.getInstance(gZone);
        DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }
}
