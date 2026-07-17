package com.lim0205.buscast.repository;

import com.lim0205.buscast.dto.prediction.QueueStatisticsProjection;
import com.lim0205.buscast.entity.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.lim0205.buscast.dto.prediction.QueueBonusProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long> {

    @Query(value = """
            SELECT
                route_id AS routeId,
                station_id AS stationId,
                day_type AS dayType,
                CAST(time_slot AS time) AS timeSlot,
                AVG(actual_queue) AS avgQueue,
                COUNT(*) AS sampleCount
            FROM prediction_history
            WHERE actual_queue IS NOT NULL
              AND day_type IS NOT NULL
              AND time_slot IS NOT NULL
              AND TRIM(time_slot) <> ''
            GROUP BY
                route_id,
                station_id,
                day_type,
                time_slot
            """,
            nativeQuery = true)
    List<QueueStatisticsProjection> calculateQueueStatistics();

    @Query(value = """
        SELECT
            route_id AS routeId,
            station_id AS stationId,
            day_type AS dayType,
            CAST(time_slot AS time) AS timeSlot,
            AVG(actual_queue) AS avgActualQueue,
            COUNT(*) AS sampleCount
        FROM prediction_history
        WHERE actual_queue IS NOT NULL
          AND day_type IS NOT NULL
          AND time_slot IS NOT NULL
          AND TRIM(time_slot) <> ''
        GROUP BY
            route_id,
            station_id,
            day_type,
            time_slot
        """,
            nativeQuery = true)
    List<QueueBonusProjection> calculateQueueBonus();

    int deleteByPredictionTimeBefore(LocalDateTime cutoff);
}
