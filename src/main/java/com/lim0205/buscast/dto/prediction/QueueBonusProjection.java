package com.lim0205.buscast.dto.prediction;

import java.time.LocalTime;

public interface QueueBonusProjection {

    Long getRouteId();

    Long getStationId();

    Short getDayType();

    LocalTime getTimeSlot();

    Double getAvgActualQueue();

    Long getSampleCount();
}