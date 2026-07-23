package com.lim0205.buscast.dto.prediction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record PredictionResult(
        @Schema(description = "예측 결과 ID", example = "101")
        Long predictionId,

        @Schema(description = "노선 ID", example = "200000112")
        Long routeId,

        @Schema(description = "정류장 ID", example = "228000123")
        Long stationId,

        @Schema(description = "예측 대상 차량 ID", example = "987654321")
        Long vehId,

        @Schema(description = "예측 수행 시각", example = "2026-07-18T08:25:31")
        LocalDateTime predictionTime,

        @Schema(description = "이전 시간대", example = "08:20:00")
        LocalTime previousTimeSlot,

        @Schema(description = "다음 시간대", example = "08:30:00")
        LocalTime nextTimeSlot,

        @Schema(description = "이전 시간대 가중치", example = "0.75")
        BigDecimal previousWeight,

        @Schema(description = "다음 시간대 가중치", example = "0.25")
        BigDecimal nextWeight,

        @Schema(description = "사용자 앞 대기 인원", example = "5")
        Integer userQueue,

        @Schema(description = "통계 기반 평균 대기 인원", example = "7.8")
        BigDecimal statisticsQueue,

        @Schema(description = "최종 예측 대기 인원", example = "6.4")
        BigDecimal predictedQueue,

        @Schema(description = "예상 잔여 좌석 수", example = "4")
        BigDecimal predictedRemainSeat,

        @Schema(description = "예측 승차 확률(%)", example = "82.5")
        BigDecimal predictedProbability,

        @Schema(description = "예측 신뢰도", example = "0.91")
        BigDecimal confidenceScore
) {
}
