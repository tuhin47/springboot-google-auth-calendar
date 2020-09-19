package com.tuhin.util;

import com.google.api.client.util.DateTime;

import java.util.Calendar;
import java.util.Date;

public class EssentialTimes {

    public static DateTime getTodaysInitialTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new DateTime(calendar.getTimeInMillis());
    }

    public static DateTime getTodaysEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTodaysInitialTime().getValue());
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return new DateTime(calendar.getTimeInMillis());
    }
}
