package com.tuhin.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.util.Comparator;

public class EventComparatorLessEndFirstWithSameStart implements Comparator<Event> {

    private int compareDateTime(DateTime dt1, DateTime dt2) {
        return Long.compare(dt1.getValue(), dt2.getValue());
    }

    private int compareStartTime(Event e1, Event e2) {
        return compareDateTime(e1.getStart().getDate(), e2.getStart().getDate());
    }

    private int compareEndTime(Event e1, Event e2) {
        return compareDateTime(e1.getEnd().getDate(), e2.getEnd().getDate());
    }

    @Override
    public int compare(Event t1, Event t2) {

        if (compareStartTime(t1, t2) == 0) {
            return compareEndTime(t1, t2);
        }
        return 0;
    }
}
