package com.lim0205.buscast.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "bus_snapshot",
        indexes = {
                @Index(name = "idx_route_time", columnList = "route_id,collected_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BusSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @Column(name = "veh_id")
    private Long vehId;

    @Column(name = "plate_no", length = 20)
    private String plateNo;

    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "station_id")
    private Long stationId;

    @Column(name = "station_seq")
    private Integer stationSeq;

    @Column(name = "remain_seat_cnt")
    private Integer remainSeatCnt;

    @Column(name = "state_cd")
    private short stateCd;
}