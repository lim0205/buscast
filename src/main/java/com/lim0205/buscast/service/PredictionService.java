package com.lim0205.buscast.service;

import com.lim0205.buscast.dto.prediction.PredictionFeedbackRequest;
import com.lim0205.buscast.dto.prediction.PredictionResult;
import com.lim0205.buscast.entity.BusSnapshot;
import com.lim0205.buscast.entity.PredictionHistory;
import com.lim0205.buscast.entity.RouteStation;
import com.lim0205.buscast.entity.StationStatistics;
import com.lim0205.buscast.repository.BusSnapshotRepository;
import com.lim0205.buscast.repository.PredictionHistoryRepository;
import com.lim0205.buscast.repository.RouteStationRepository;
import com.lim0205.buscast.repository.StationStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private static final BigDecimal THIRTY = BigDecimal.valueOf(30);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int CURRENT_BUS_LOOKBACK_MINUTES = 15;
    private static final short RULE_BASED_PREDICTION = 0;

    private final StationStatisticsRepository stationStatisticsRepository;
    private final RouteStationRepository routeStationRepository;
    private final BusSnapshotRepository busSnapshotRepository;
    private final PredictionHistoryRepository predictionHistoryRepository;

    @Transactional
    public PredictionResult predict(long routeId, long stationId) {
        return predict(routeId, stationId, null, null);
    }

    @Transactional
    public PredictionResult predict(long routeId, long stationId, Integer userQueue) {
        return predict(routeId, stationId, userQueue, null);
    }

    @Transactional
    public PredictionResult predict(long routeId, long stationId, Integer userQueue, LocalDateTime targetTime) {
        if (targetTime == null) {
            targetTime = LocalDateTime.now();
        }
        BusSnapshot currentBus = findCurrentBus(routeId, stationId, targetTime).orElse(null);
        LocalTime previousTimeSlot = getPreviousTimeSlot(targetTime);
        LocalTime nextTimeSlot = previousTimeSlot.plusMinutes(30);
        BigDecimal nextWeight = BigDecimal.valueOf(targetTime.getMinute() % 30)
                .divide(THIRTY, 4, RoundingMode.HALF_UP);
        BigDecimal previousWeight = BigDecimal.ONE.subtract(nextWeight);

        Short dayType = getDayType(targetTime);
        StationStatistics previous = findStatistics(routeId, stationId, dayType, previousTimeSlot);
        StationStatistics next = findStatistics(routeId, stationId, dayType, nextTimeSlot);

        BigDecimal interpolatedAvgQueue = interpolate(
                previous == null ? null : previous.getAvgQueue(),
                next == null ? null : next.getAvgQueue(),
                previousWeight,
                nextWeight
        );
        BigDecimal interpolatedQueueBonus = interpolate(
                previous == null ? null : previous.getQueueBonus(),
                next == null ? null : next.getQueueBonus(),
                previousWeight,
                nextWeight
        );
        BigDecimal predictedQueue = calculatePredictedQueue(userQueue,
                interpolatedAvgQueue,
                interpolatedQueueBonus,
                currentBus);
        BigDecimal predictedRemainSeat = calculatePredictedRemainSeat(
                currentBus,
                interpolate(
                previous == null ? null : previous.getAvgRemainSeat(),
                next == null ? null : next.getAvgRemainSeat(),
                previousWeight,
                nextWeight
                )
        );
        BigDecimal confidenceScore = interpolate(
                previous == null ? null : previous.getConfidenceScore(),
                next == null ? null : next.getConfidenceScore(),
                previousWeight,
                nextWeight
        );
        BigDecimal predictedProbability = calculateProbability(predictedRemainSeat, predictedQueue);

        PredictionHistory history = predictionHistoryRepository.save(
                PredictionHistory.builder()
                        .routeId(routeId)
                        .stationId(stationId)
                        .vehId(currentBus == null ? null : currentBus.getVehId())
                        .dayType(dayType)
                        .timeSlot(previousTimeSlot.toString())
                        .userQueue(userQueue)
                        .remainSeatCnt(currentBus == null ? null : currentBus.getRemainSeatCnt())
                        .predictedQueue(predictedQueue)
                        .predictedRemainSeat(predictedRemainSeat)
                        .predictedProbability(predictedProbability)
                        .predictionType(RULE_BASED_PREDICTION)
                        .predictionTime(targetTime)
                        .build()
        );

        return new PredictionResult(
                history.getId(),
                routeId,
                stationId,
                currentBus == null ? null : currentBus.getVehId(),
                targetTime,
                previousTimeSlot,
                nextTimeSlot,
                previousWeight.multiply(HUNDRED).setScale(2, RoundingMode.HALF_UP),
                nextWeight.multiply(HUNDRED).setScale(2, RoundingMode.HALF_UP),
                userQueue,
                interpolatedAvgQueue,
                predictedQueue,
                predictedRemainSeat,
                predictedProbability,
                confidenceScore
        );
    }

    @Transactional
    public void updateFeedback(Long predictionId, PredictionFeedbackRequest request) {
        PredictionHistory history = predictionHistoryRepository.findById(predictionId)
                .orElseThrow(() -> new IllegalArgumentException("Prediction history not found: " + predictionId));

        history.updateFeedback(
                request.userQueue(),
                request.actualQueue(),
                request.boardResult(),
                LocalDateTime.now()
        );
    }

    private Optional<BusSnapshot> findCurrentBus(Long routeId, Long stationId, LocalDateTime targetTime) {
        Optional<RouteStation> routeStation =
                routeStationRepository.findFirstByRouteIdAndStationId(routeId, stationId);

        if (routeStation.isEmpty()) {
            return Optional.empty();
        }

        return busSnapshotRepository.findCurrentBusBeforeStation(
                routeId,
                routeStation.get().getStationSeq(),
                targetTime.minusMinutes(CURRENT_BUS_LOOKBACK_MINUTES)
        );
    }

    private StationStatistics findStatistics(Long routeId, Long stationId, Short dayType, LocalTime timeSlot) {
        Optional<StationStatistics> statistics =
                stationStatisticsRepository.findByRouteIdAndStationIdAndDayTypeAndTimeSlot(
                        routeId,
                        stationId,
                        dayType,
                        timeSlot
                );

        return statistics.orElse(null);
    }

    private BigDecimal interpolate(
            BigDecimal previousValue,
            BigDecimal nextValue,
            BigDecimal previousWeight,
            BigDecimal nextWeight
    ) {
        if (previousValue == null && nextValue == null) {
            return null;
        }

        if (previousValue == null) {
            return nextValue.setScale(2, RoundingMode.HALF_UP);
        }

        if (nextValue == null) {
            return previousValue.setScale(2, RoundingMode.HALF_UP);
        }

        return previousValue.multiply(previousWeight)
                .add(nextValue.multiply(nextWeight))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePredictedRemainSeat(BusSnapshot currentBus, BigDecimal interpolatedRemainSeat) {
        if (currentBus == null
                || currentBus.getRemainSeatCnt() == null
                || currentBus.getRemainSeatCnt() == -1) {
            return interpolatedRemainSeat;
        }

        return BigDecimal.valueOf(currentBus.getRemainSeatCnt())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePredictedQueue(
            Integer userQueue,
            BigDecimal avgQueue,
            BigDecimal queueBonus,
            BusSnapshot currentBus
    ) {
        if (userQueue != null) {
            return BigDecimal.valueOf(userQueue)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (avgQueue == null) {
            return null;
        }

        if (currentBus != null
                && currentBus.getRemainSeatCnt() != null
                && currentBus.getRemainSeatCnt() == 0
                && queueBonus != null) {

            return avgQueue.add(queueBonus)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return avgQueue.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateProbability(BigDecimal remainSeat, BigDecimal avgQueue) {
        if (remainSeat == null || avgQueue == null) {
            return null;
        }

        if (avgQueue.compareTo(BigDecimal.ZERO) == 0) {
            return HUNDRED.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal rate = remainSeat.max(BigDecimal.ZERO)
                .divide(avgQueue, 4, RoundingMode.HALF_UP)
                .multiply(HUNDRED);

        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (rate.compareTo(HUNDRED) > 0) {
            return HUNDRED.setScale(2, RoundingMode.HALF_UP);
        }

        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    private LocalTime getPreviousTimeSlot(LocalDateTime targetTime) {
        int minute = targetTime.getMinute() < 30 ? 0 : 30;

        return LocalTime.of(targetTime.getHour(), minute);
    }

    private Short getDayType(LocalDateTime targetTime) {
        DayOfWeek dayOfWeek = targetTime.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return 1;
        }

        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return 2;
        }

        return 0;
    }
}
