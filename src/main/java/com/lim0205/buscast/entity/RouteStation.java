package com.lim0205.buscast.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Entity
@Table(
        name = "route_station",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_route_station",
                        columnNames = {
                                "route_id",
                                "up_down",
                                "station_seq"
                        }
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RouteStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "station_id", nullable = false)
    private Long stationId;

    @Column(name = "station_seq", nullable = false)
    private Integer stationSeq;

    @Column(name = "up_Down", nullable = false, length = 10)
    private String upDown;
}