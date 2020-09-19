package com.tuhin.calendar;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.tuhin.util.EssentialTimes.getTodaysEndTime;
import static com.tuhin.util.EssentialTimes.getTodaysInitialTime;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "CalendarApp";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;


    public ResponseEntity<String> getCalendarEvents(String code) {
        String message;
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            Credential credential = flow.createAndStoreCredential(response, "userID");
            Calendar client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            Calendar.Events events = client.events();
            Events  eventList = events.list("primary")
                    .setTimeMin(getTodaysInitialTime()).setTimeMax(getTodaysEndTime())
                    .execute();
            List<Event> todayEvents = eventList.getItems();
            if (todayEvents.size() > 0) {
                todayEvents.sort(new EventComparatorFirstStartLessEnd());
                System.out.println(todayEvents);
            }
            message = eventList.getItems().toString();
            System.out.println("My:" + eventList.getItems());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            message = "Exception Handling Calendar Data";
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    protected String authorize() throws Exception {
        if (flow == null) {
            Details web = new Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR_READONLY)).build();
        }
        AuthorizationCodeRequestUrl requestUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
        System.out.println("Request Url" + requestUrl);
        return requestUrl.build();
    }
}