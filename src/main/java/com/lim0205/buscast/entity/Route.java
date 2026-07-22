package com.lim0205.buscast.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "route_name", length = 20, nullable = false)
    private String routeName;

    @Column(name = "route_type_cd")
    private Integer routeTypeCd;

    @Column(name = "start_station_id")
    private Long startStationId;

    @Column(name = "end_station_id")
    private Long endStationId;

    @Column(name = "up_first_time")
    private LocalTime upFirstTime;

    @Column(name = "up_last_time")
    private LocalTime upLastTime;

    @Column(name = "down_first_time")
    private LocalTime downFirstTime;

    @Column(name = "down_last_time")
    private LocalTime downLastTime;

    @Column(name = "peak_alloc")
    private Integer peakAlloc;

    @Column(name = "non_peak_alloc")
    private Integer nonPeakAlloc;

    @Column(name = "sat_up_first_time", length = 5)
    private LocalTime satUpFirstTime;

    @Column(name = "sat_up_last_time", length = 5)
    private LocalTime satUpLastTime;

    @Column(name = "sat_down_first_time", length = 5)
    private LocalTime satDownFirstTime;

    @Column(name = "sat_down_last_time", length = 5)
    private LocalTime satDownLastTime;

    @Column(name = "sat_peak_alloc")
    private Integer satPeakAlloc;

    @Column(name = "sat_non_peak_alloc")
    private Integer satNonPeakAlloc;

    @Column(name = "sun_up_first_time", length = 5)
    private LocalTime sunUpFirstTime;

    @Column(name = "sun_up_last_time", length = 5)
    private LocalTime sunUpLastTime;

    @Column(name = "sun_down_first_time", length = 5)
    private LocalTime sunDownFirstTime;

    @Column(name = "sun_down_last_time", length = 5)
    private LocalTime sunDownLastTime;

    @Column(name = "sun_peak_alloc")
    private Integer sunPeakAlloc;

    @Column(name = "sun_non_peak_alloc")
    private Integer sunNonPeakAlloc;
}