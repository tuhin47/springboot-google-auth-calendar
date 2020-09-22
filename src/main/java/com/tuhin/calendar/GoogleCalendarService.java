package com.tuhin.calendar;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import static com.tuhin.util.TimeEssentials.*;

@Log4j2
@Service
public class GoogleCalendarService extends GoogleCalendarEssentials {

    protected String authorizeURL() throws Exception {
        AuthorizationCodeRequestUrl requestUrl = getGoogleAuthorizationCodeFlow()
                .newAuthorizationUrl().setRedirectUri(getRedirectURI());
        log.info("Request Url" + requestUrl);
        return requestUrl.build();
    }

    public void getCalendarEvents(String code, RedirectAttributes redirectAttributes) {
        try {
            List<Event> todayEvents = getCalendarEventsOfToday(code);
            List<EventViewDTO> occupiedList = getOccupiedListOfToday(todayEvents);
            List<EventViewDTO> availableList = getAvailableListOfToday(occupiedList);
            redirectAttributes.addFlashAttribute("tasks", occupiedList);
            redirectAttributes.addFlashAttribute("available", availableList);
            log.info("Occupied:" + occupiedList.toString());
            log.info("Available:" + availableList.toString());
        } catch (Exception e) {
            log.error("Exception to get calendar data" + e.getMessage());
        }
    }

     List<EventViewDTO> getAvailableListOfToday(List<EventViewDTO> occupiedList) {
         HashMap<Integer, TimePeriod> mergedOccupied = getMergedOccupiedList(occupiedList);
         return getAvailAbleListFromMergedOccupied(mergedOccupied);
     }

    private List<EventViewDTO> getAvailAbleListFromMergedOccupied(HashMap<Integer, TimePeriod> mergedOccupied) {
        Iterator it = mergedOccupied.entrySet().iterator();
        long start = getInitialTimeOfToday().getValue();
        long end = getEndTimeOfToday().getValue();
        List<EventViewDTO> availableList = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            log.info(pair.getKey() + " = " + pair.getValue());
            TimePeriod time = (TimePeriod) pair.getValue();
            long period = time.getStartTime() - start;
            if (period > 0) {
                availableList.add(
                        EventViewDTO.builder()
                                .startTime(start)
                                .endTime(time.getStartTime() - 1)
                                .timePeriod(getTimePeriodStringByStartAndEnd(start, time.getStartTime() - 1))
                                .summary("available")
                                .build()
                );
            }
            start = Math.max(start, time.getEndTime() + 1);
        }
        if (end - start > 0) {
            availableList.add(
                    EventViewDTO.builder().startTime(start).endTime(end)
                            .timePeriod(getTimePeriodStringByStartAndEnd(start, end))
                            .summary("available").build()
            );
        }
        return availableList;
    }

    private HashMap<Integer, TimePeriod> getMergedOccupiedList(List<EventViewDTO> occupiedList) {
        HashMap<Integer, TimePeriod> mergedOccupied = new HashMap<>();
        for (EventViewDTO event : occupiedList) {
            Iterator it = mergedOccupied.entrySet().iterator();
            boolean inserted = false;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                TimePeriod time = (TimePeriod) pair.getValue();
                if (inBetweenTime(event.getStartTime(), time) || inBetweenTime(event.getEndTime(), time)) {
                    inserted = true;
                    TimePeriod timePeriod = TimePeriod.builder().
                            startTime(Math.min(time.getStartTime(), event.getStartTime()))
                            .endTime(Math.max(time.getEndTime(), event.getEndTime()))
                            .build();
                    mergedOccupied.put((Integer) pair.getKey(), timePeriod);
                }

            }
            if (!inserted) {
                TimePeriod timePeriod = TimePeriod.builder().
                        startTime(event.getStartTime())
                        .endTime(event.getEndTime())
                        .build();
                mergedOccupied.put(mergedOccupied.size(), timePeriod);
            }
        }
        return mergedOccupied;
    }

    List<EventViewDTO> getOccupiedListOfToday(List<Event> todayEvents) {
        List<EventViewDTO> viewDTOS = new ArrayList<>();
        for (Event event : todayEvents) {
            EventViewDTO viewDTO = EventViewDTO.builder()
                    .id(event.getId())
                    .startTime(event.getStart().getDateTime().getValue())
                    .endTime(event.getEnd().getDateTime().getValue())
                    .timePeriod(getTimePeriodStringByEvent(event))
                    .summary(event.getSummary())
                    .build();
            viewDTOS.add(viewDTO);
        }
        return viewDTOS;
    }

    private List<Event> getCalendarEventsOfToday(String code) throws IOException, GeneralSecurityException {
        TokenResponse response = getGoogleAuthorizationCodeFlow().newTokenRequest(code).setRedirectUri(getRedirectURI()).execute();
        Credential credential = getGoogleAuthorizationCodeFlow().createAndStoreCredential(response, "userID");
        Calendar client = new Calendar.Builder(getHttpTransport(), getJsonFactory(), credential)
                .setApplicationName(getApplicationName()).build();
        Calendar.Events events = client.events();
        Events eventList = events.list("primary")
                .setTimeMin(getInitialTimeOfToday()).setTimeMax(getEndTimeOfToday())
                .execute();
        List<Event> todayEvents = eventList.getItems();
        if (todayEvents.size() > 0) {
            todayEvents.sort(new EventComparatorFirstStartLessEnd());
        }
        return todayEvents;
    }


}