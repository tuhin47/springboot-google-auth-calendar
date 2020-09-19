package com.tuhin.calendar;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimePeriod {
    private long startTime;
    private long endTime;
}
