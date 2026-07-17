package com.lim0205.buscast.service;

import com.lim0205.buscast.dto.prediction.QueueStatisticsProjection;
import com.lim0205.buscast.dto.prediction.StatisticsProjection;
import com.lim0205.buscast.entity.StationStatistics;
import com.lim0205.buscast.repository.BusSnapshotRepository;
import com.lim0205.buscast.repository.PredictionHistoryRepository;
import com.lim0205.buscast.repository.StationStatisticsRepository;
import com.lim0205.buscast.statistics.BoardingCalculator;
import com.lim0205.buscast.statistics.BoardingStatistics;
import com.lim0205.buscast.statistics.QueueStatistics;
import com.lim0205.buscast.statistics.StatisticsKey;
import com.lim0205.buscast.dto.prediction.QueueBonusProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private static final String JOB_NAME = "station-statistics";
    private static final int PREDICTION_HISTORY_RETENTION_DAYS = 5;
    private static final BigDecimal MAX_USER_QUEUE_WEIGHT = BigDecimal.valueOf(0.7);
    private static final BigDecimal USER_QUEUE_FULL_SAMPLE_COUNT = BigDecimal.valueOf(30);
    private static final BigDecimal CONFIDENCE_FULL_SAMPLE_COUNT = BigDecimal.valueOf(30);

    private final BusSnapshotRepository busSnapshotRepository;
    private final StationStatisticsRepository stationStatisticsRepository;
    private final BoardingCalculator boardingCalculator;
    private final PredictionHistoryRepository predictionHistoryRepository;
    private final BatchHistoryService batchHistoryService;


    @Transactional
    public void calculateStatistics() {
        LocalDateTime startedAt = LocalDateTime.now();

        try {
            calculateAndSaveStatistics(startedAt);
        } catch (RuntimeException e) {
            batchHistoryService.record(
                    JOB_NAME,
                    startedAt,
                    BatchHistoryService.STATUS_FAILED,
                    0,
                    e.getMessage()
            );
            throw e;
        }
    }

    private void calculateAndSaveStatistics(LocalDateTime startedAt) {
        Map<StatisticsKey, BoardingStatistics> boardingStatistics =
                boardingCalculator.calculate();
        Map<StatisticsKey, QueueStatistics> queueStatistics =
                calculateQueueStatistics();
        Map<StatisticsKey, BigDecimal> queueBonusMap =
                calculateQueueBonus();

        List<StatisticsProjection> projections =
                busSnapshotRepository.calculateRemainSeatStatistics();

        List<StationStatistics> statistics = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (StatisticsProjection p : projections) {
            StatisticsKey key = new StatisticsKey(
                    p.getRouteId(),
                    p.getStationId(),
                    p.getDayType(),
                    p.getTimeSlot()
            );
            BoardingStatistics boarding = boardingStatistics.get(key);
            QueueStatistics userQueue = queueStatistics.get(key);
            BigDecimal avgBoarding = boarding == null ? null : boarding.avgBoarding();
            BigDecimal avgQueue = calculateAvgQueue(boarding, userQueue);
            BigDecimal queueBonus =
                    calculateQueueBonus(
                            avgQueue,
                            queueBonusMap.get(key)
                    );

            StationStatistics statistic = StationStatistics.builder()
                    .routeId(p.getRouteId())
                    .stationId(p.getStationId())
                    .dayType(p.getDayType())
                    .timeSlot(p.getTimeSlot())
                    .avgQueue(avgQueue)
                    .avgRemainSeat(p.getAvgRemainSeat())
                    .avgBoarding(avgBoarding)
                    .queueBonus(queueBonus)
                    .sampleCount(p.getSampleCount().intValue())
                    .confidenceScore(calculateConfidenceScore(
                            p.getSampleCount().intValue(),
                            boarding == null ? 0 : boarding.sampleCount(),
                            userQueue == null ? 0 : userQueue.sampleCount()
                    ))
                    .calculatedAt(now)
                    .build();

            statistics.add(statistic);
        }

        stationStatisticsRepository.deleteAllInBatch();
        stationStatisticsRepository.saveAll(statistics);

        int deletedPredictionHistoryCount =
                predictionHistoryRepository.deleteByPredictionTimeBefore(
                        now.minusDays(PREDICTION_HISTORY_RETENTION_DAYS)
                );

        batchHistoryService.record(
                JOB_NAME,
                startedAt,
                BatchHistoryService.STATUS_SUCCESS,
                statistics.size(),
                "statistics=" + statistics.size()
                        + ", deletedPredictionHistory=" + deletedPredictionHistoryCount
        );
    }

    private BigDecimal calculateQueueBonus(
            BigDecimal avgQueue,
            BigDecimal avgActualQueue
    ) {

        if (avgQueue == null || avgActualQueue == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal rawBonus = avgActualQueue.subtract(avgQueue);

        if (rawBonus.compareTo(BigDecimal.ZERO) < 0) {
            rawBonus = BigDecimal.ZERO;
        }

        if (rawBonus.compareTo(BigDecimal.valueOf(30)) > 0) {
            rawBonus = BigDecimal.valueOf(30);
        }

        return rawBonus.setScale(2, RoundingMode.HALF_UP);
    }

    private Map<StatisticsKey, QueueStatistics> calculateQueueStatistics() {
        List<QueueStatisticsProjection> projections =
                predictionHistoryRepository.calculateQueueStatistics();

        Map<StatisticsKey, QueueStatistics> result = new HashMap<>();

        for (QueueStatisticsProjection p : projections) {
            StatisticsKey key = new StatisticsKey(
                    p.getRouteId(),
                    p.getStationId(),
                    p.getDayType(),
                    p.getTimeSlot()
            );

            result.put(
                    key,
                    new QueueStatistics(
                            p.getAvgQueue(),
                            p.getSampleCount().intValue()
                    )
            );
        }

        return result;
    }

    private Map<StatisticsKey, BigDecimal> calculateQueueBonus() {

        List<QueueBonusProjection> projections =
                predictionHistoryRepository.calculateQueueBonus();

        Map<StatisticsKey, BigDecimal> result = new HashMap<>();

        for (QueueBonusProjection p : projections) {

            StatisticsKey key = new StatisticsKey(
                    p.getRouteId(),
                    p.getStationId(),
                    p.getDayType(),
                    p.getTimeSlot()
            );

            // 표본이 너무 적으면 학습하지 않음
            if (p.getSampleCount() < 5) {
                result.put(key, BigDecimal.ZERO);
                continue;
            }

            result.put(
                    key,
                    BigDecimal.valueOf(p.getAvgActualQueue())
            );
        }

        return result;
    }

    private BigDecimal calculateAvgQueue(BoardingStatistics boarding, QueueStatistics userQueue) {
        BigDecimal estimatedQueue = calculateEstimatedQueue(boarding);

        if (userQueue == null || userQueue.avgQueue() == null) {
            return estimatedQueue;
        }

        if (estimatedQueue == null) {
            return userQueue.avgQueue().setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal userWeight = BigDecimal.valueOf(userQueue.sampleCount())
                .divide(USER_QUEUE_FULL_SAMPLE_COUNT, 4, RoundingMode.HALF_UP)
                .min(MAX_USER_QUEUE_WEIGHT);
        BigDecimal estimateWeight = BigDecimal.ONE.subtract(userWeight);

        return estimatedQueue.multiply(estimateWeight)
                .add(userQueue.avgQueue().multiply(userWeight))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateEstimatedQueue(BoardingStatistics boarding) {
        if (boarding == null || boarding.avgBoarding() == null) {
            return null;
        }

        BigDecimal effectiveBoarding = boarding.avgBoarding().max(BigDecimal.ZERO);
        return boarding.avgBoarding()
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateConfidenceScore(
            int remainSeatSampleCount,
            int boardingSampleCount,
            int queueSampleCount
    ) {
        BigDecimal remainSeatScore = calculateSampleScore(remainSeatSampleCount);
        BigDecimal boardingScore = calculateSampleScore(boardingSampleCount);
        BigDecimal queueScore = calculateSampleScore(queueSampleCount);

        return remainSeatScore
                .add(boardingScore)
                .add(queueScore)
                .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSampleScore(int sampleCount) {
        BigDecimal score = BigDecimal.valueOf(sampleCount)
                .divide(CONFIDENCE_FULL_SAMPLE_COUNT, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return score.min(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
