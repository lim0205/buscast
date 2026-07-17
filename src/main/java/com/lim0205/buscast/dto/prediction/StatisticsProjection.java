package com.lim0205.buscast.dto.prediction;

import java.math.BigDecimal;
import java.time.LocalTime;

public interface StatisticsProjection {

    Long getRouteId();

    Long getStationId();

    Short getDayType();

    LocalTime getTimeSlot();

    BigDecimal getAvgRemainSeat();

    Long getSampleCount();
}