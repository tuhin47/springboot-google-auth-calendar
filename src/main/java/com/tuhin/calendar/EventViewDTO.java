package com.tuhin.calendar;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class EventViewDTO {

    private String id;
    private String timePeriod;
    private String summary;
    private long startTime;
    private long endTime;

}
