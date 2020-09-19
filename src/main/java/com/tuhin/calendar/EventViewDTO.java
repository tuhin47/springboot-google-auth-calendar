package com.tuhin.calendar;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventViewDTO {

    private String id;
    private String timePeriod;
    private String summary;

    @Override
    public String toString() {
        return "EventViewDTO{" +
                "id='" + id + '\'' +
                ", timePeriod='" + timePeriod + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
