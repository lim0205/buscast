package com.lim0205.buscast.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long routeId;

    private Long stationId;

    private Long vehId;

    private Short dayType;

    @Column(length = 10)
    private String timeSlot;

    @Column(precision = 5, scale = 2)
    private BigDecimal predictedQueue;

    private Integer actualQueue;

    @Column(precision = 5, scale = 2)
    private BigDecimal predictedRemainSeat;

    @Column(precision = 5, scale = 2)
    private BigDecimal predictedProbability;

    private boolean boardResult;

    private Short predictionType;

    private LocalDateTime predictionTime;

    private LocalDateTime feedbackTime;
}