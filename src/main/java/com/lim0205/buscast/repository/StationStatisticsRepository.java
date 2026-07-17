package com.lim0205.buscast.repository;

import com.lim0205.buscast.entity.StationStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Optional;

public interface StationStatisticsRepository extends JpaRepository<StationStatistics, Long> {

    Optional<StationStatistics> findByRouteIdAndStationIdAndDayTypeAndTimeSlot(
            Long routeId,
            Long stationId,
            Short dayType,
            LocalTime timeSlot
    );
}
