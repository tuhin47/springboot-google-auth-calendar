package com.tuhin.calendar;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.tuhin.util.CommonUtils;
import com.tuhin.util.TimeEssentials;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.tuhin.util.TimeEssentials.*;

class EventComparatorFirstStartLessEndTest {


    @Test
    void testSortingStartDateWise() {
        List<Event> events = new ArrayList<>();
        long start = TimeEssentials.getInitialTimeOfToday().getValue();
        long end = TimeEssentials.getEndTimeOfToday().getValue();

        List<Long> arr = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            long startTime = CommonUtils.getRandomNumber(start, end);
            arr.add(startTime);
            Event event = new Event();
            event.setStart(new EventDateTime().setDateTime(getDateTimeFromMS(startTime)));
            events.add(event);
        }
        events.sort(new EventComparatorFirstStartLessEnd());
        Collections.sort(arr);
        int i = 0;
        for (Event event : events) {
            Assert.assertEquals((Long) event.getStart().getDateTime().getValue(), arr.get(i++));
        }


    }

    @Test
    void testSortingSameStartDateLessEndDate() {
        List<Event> events = new ArrayList<>();
        long start = TimeEssentials.getInitialTimeOfToday().getValue();
        long end = TimeEssentials.getEndTimeOfToday().getValue();

        List<Long> arr = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Event event = new Event();
            event.setStart(new EventDateTime().setDateTime(getDateTimeFromMS(start)));
            long endTime = CommonUtils.getRandomNumber(start, end);
            arr.add(endTime);
            event.setEnd(new EventDateTime().setDateTime(getDateTimeFromMS(endTime)));
            events.add(event);
        }
        events.sort(new EventComparatorFirstStartLessEnd());
        Collections.sort(arr);
        int i = 0;
        for (Event event : events) {
            Assert.assertEquals((Long) event.getEnd().getDateTime().getValue(), arr.get(i++));
        }


    }

}