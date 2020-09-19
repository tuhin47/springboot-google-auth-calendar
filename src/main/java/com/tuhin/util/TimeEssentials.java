package com.tuhin.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.tuhin.calendar.TimePeriod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeEssentials {
    public static final DateFormat HOUR_MINUTE_FORMAT = new SimpleDateFormat("hh:mm a");

    public static String getHourMinuteFormat(long time) {
        Date applicationDate = new Date(time);
        HOUR_MINUTE_FORMAT.setLenient(false);
        return HOUR_MINUTE_FORMAT.format(applicationDate);
    }

    public static String getHourMinuteFormat(DateTime time) {
        return getHourMinuteFormat(time.getValue());
    }

    public static DateTime getInitialTimeOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new DateTime(calendar.getTimeInMillis());
    }

    public static DateTime getEndTimeOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getInitialTimeOfToday().getValue());
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return new DateTime(calendar.getTimeInMillis());
    }


    public static String getTimePeriodStringByEvent(Event event) {
        return getHourMinuteFormat(event.getStart().getDateTime()) +
                " - " +
                getHourMinuteFormat(event.getEnd().getDateTime());
    }

    public static String getTimePeriodStringByStartAndEnd(long start, long end) {
        return getHourMinuteFormat(start) +
                " - " +
                getHourMinuteFormat(end);
    }

    public static boolean inBetweenTime(long value, TimePeriod time) {
        return value >= time.getStartTime() && value <= time.getStartTime();
    }
}
