package com.lim0205.buscast.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "station_statistics",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "route_id",
                                "station_id",
                                "day_type",
                                "time_slot"
                        }
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StationStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long routeId;

    private Long stationId;

    private Short dayType;

    @Column(length = 10)
    private String timeSlot;

    private BigDecimal avgQueue;

    private BigDecimal avgBoarding;

    private BigDecimal avgRemainSeat;

    @Column(precision = 5, scale = 2)
    private BigDecimal boardingSuccessRate;

    private Integer sampleCount;

    @Column(precision = 5, scale = 2)
    private BigDecimal confidenceScore;

    private LocalDateTime calculatedAt;
}