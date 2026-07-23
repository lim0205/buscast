package com.lim0205.buscast.dto.prediction;

import java.math.BigDecimal;
import java.time.LocalTime;

public interface QueueStatisticsProjection {

    Long getRouteId();

    Long getStationId();

    Short getDayType();

    LocalTime getTimeSlot();

    BigDecimal getAvgQueue();

    Long getSampleCount();
}
