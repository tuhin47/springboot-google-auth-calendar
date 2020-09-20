package com.tuhin.util;

import com.google.api.client.util.DateTime;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static com.tuhin.util.TimeEssentials.*;
import static org.junit.jupiter.api.Assertions.*;

class TimeEssentialsTest {

    @Test
    void getInitialTimeOfTodayTest() {
        DateTime dateTime = getInitialTimeOfToday();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime.getValue());

        Date system = new Date();
        assertEquals(calendar.getTime().getMonth(), system.getMonth());
        assertEquals(calendar.getTime().getDate(), system.getDate());
        assertEquals(calendar.getTime().getHours(), 0);
        assertEquals(calendar.getTime().getMinutes(), 0);
        assertEquals(calendar.getTime().getSeconds(), 0);
    }

    @Test
    void getEndTimeOfTodayTest() {
        DateTime dateTime = getEndTimeOfToday();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime.getValue());

        Date system = new Date();
        assertEquals(calendar.getTime().getMonth(), system.getMonth());
        assertEquals(calendar.getTime().getDate(), system.getDate());
        assertEquals(calendar.getTime().getHours(), 23);
        assertEquals(calendar.getTime().getMinutes(), 59);
        assertEquals(calendar.getTime().getSeconds(), 59);
    }

}