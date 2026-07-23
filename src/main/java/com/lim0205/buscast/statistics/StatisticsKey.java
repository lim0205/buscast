package com.lim0205.buscast.statistics;

import java.time.LocalTime;

public record StatisticsKey(
        Long routeId,
        Long stationId,
        Short dayType,
        LocalTime timeSlot
) {
}
