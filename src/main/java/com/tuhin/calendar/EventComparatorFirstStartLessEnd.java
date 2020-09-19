package com.tuhin.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.util.Comparator;

public class EventComparatorFirstStartLessEnd implements Comparator<Event> {

    private int compareDateTime(DateTime dt1, DateTime dt2) {
        return Long.compare(dt1.getValue(), dt2.getValue());
    }

    private int compareStartTime(Event e1, Event e2) {
        return compareDateTime(e1.getStart().getDateTime(), e2.getStart().getDateTime());
    }

    private int compareEndTime(Event e1, Event e2) {
        return compareDateTime(e1.getEnd().getDateTime(), e2.getEnd().getDateTime());
    }

    @Override
    public int compare(Event t1, Event t2) {

        int firstStart = compareStartTime(t1, t2);
        if (firstStart == 0) {
            return compareEndTime(t1, t2);
        }
        return firstStart;
    }
}
