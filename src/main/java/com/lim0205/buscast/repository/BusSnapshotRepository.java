package com.lim0205.buscast.repository;

import com.lim0205.buscast.dto.prediction.StatisticsProjection;
import com.lim0205.buscast.entity.BusSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusSnapshotRepository extends JpaRepository<BusSnapshot, Long> {

    @Query(value = """
            SELECT
                route_id AS routeId,
                station_id AS stationId,

                CASE
                    WHEN EXTRACT(DOW FROM collected_at) BETWEEN 1 AND 5 THEN 0
                    WHEN EXTRACT(DOW FROM collected_at) = 6 THEN 1
                    ELSE 2
                END AS dayType,

                MAKE_TIME(
                    EXTRACT(HOUR FROM collected_at)::int,
                    CASE
                        WHEN EXTRACT(MINUTE FROM collected_at) < 30 THEN 0
                        ELSE 30
                    END,
                    0
                ) AS timeSlot,

                AVG(remain_seat_cnt) AS avgRemainSeat,

                COUNT(*) AS sampleCount

            FROM bus_snapshot

            WHERE remain_seat_cnt IS NOT NULL
              AND remain_seat_cnt <> -1

            GROUP BY
                route_id,
                station_id,
                dayType,
                timeSlot

            ORDER BY
                route_id,
                station_id,
                dayType,
                timeSlot
            """,
            nativeQuery = true)
    List<StatisticsProjection> calculateRemainSeatStatistics();

    List<BusSnapshot> findAllByOrderByVehIdAscCollectedAtAsc();

    @Query(value = """
            SELECT *
            FROM (
                SELECT DISTINCT ON (veh_id)
                    *
                FROM bus_snapshot
                WHERE route_id = :routeId
                  AND veh_id IS NOT NULL
                  AND collected_at >= :since
                ORDER BY veh_id, collected_at DESC
            ) latest
            WHERE station_seq <= :targetStationSeq
            ORDER BY station_seq DESC, collected_at DESC
            LIMIT 1
            """,
            nativeQuery = true)
    Optional<BusSnapshot> findCurrentBusBeforeStation(
            @Param("routeId") Long routeId,
            @Param("targetStationSeq") Integer targetStationSeq,
            @Param("since") LocalDateTime since
    );
}
