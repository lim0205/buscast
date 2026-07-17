package com.lim0205.buscast.statistics;

import com.lim0205.buscast.entity.BusSnapshot;
import com.lim0205.buscast.repository.BusSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardingCalculator {

    private final BusSnapshotRepository snapshotRepository;

    public Map<StatisticsKey, BoardingStatistics> calculate() {
        List<BusSnapshot> snapshots =
                snapshotRepository.findAllByOrderByVehIdAscCollectedAtAsc();

        return calculateBoarding(snapshots);
    }

    private Map<StatisticsKey, BoardingStatistics> calculateBoarding(List<BusSnapshot> snapshots) {
        Map<StatisticsKey, BoardingAccumulator> accumulators = new HashMap<>();

        BusSnapshot prev = null;

        for (BusSnapshot curr : snapshots) {

            if (prev == null) {
                prev = curr;
                continue;
            }

            if (prev.getVehId() == null || !prev.getVehId().equals(curr.getVehId())) {
                prev = curr;
                continue;
            }

            if (isValidBoardingPair(prev, curr)) {
                int boarding = prev.getRemainSeatCnt() - curr.getRemainSeatCnt();

                StatisticsKey key = new StatisticsKey(
                        curr.getRouteId(),
                        curr.getStationId(),
                        getDayType(curr),
                        getTimeSlot(curr)
                );

                accumulators
                        .computeIfAbsent(key, ignored -> new BoardingAccumulator())
                        .add(boarding);

                if (curr.getRemainSeatCnt() == 0) {
                    accumulators.get(key).addFullSeat();
                }
            }

            prev = curr;
        }

        Map<StatisticsKey, BoardingStatistics> result = new HashMap<>();

        for (Map.Entry<StatisticsKey, BoardingAccumulator> entry : accumulators.entrySet()) {
            BoardingAccumulator accumulator = entry.getValue();
            result.put(
                    entry.getKey(),
                    new BoardingStatistics(
                            accumulator.average(),
                            accumulator.count,
                            accumulator.fullSeatRate()
                    )
            );
        }

        return result;
    }

    private boolean isValidBoardingPair(BusSnapshot prev, BusSnapshot curr) {
        return prev.getStationSeq() != null
                && curr.getStationSeq() != null
                && curr.getStationSeq() == prev.getStationSeq() + 1
                && prev.getRemainSeatCnt() != null
                && curr.getRemainSeatCnt() != null
                && prev.getRemainSeatCnt() != -1
                && curr.getRemainSeatCnt() != -1
                && curr.getRouteId() != null
                && curr.getStationId() != null
                && curr.getCollectedAt() != null;
    }

    private Short getDayType(BusSnapshot snapshot) {
        DayOfWeek dayOfWeek = snapshot.getCollectedAt().getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return 1;
        }

        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return 2;
        }

        return 0;
    }

    private LocalTime getTimeSlot(BusSnapshot snapshot) {
        int minute = snapshot.getCollectedAt().getMinute() < 30 ? 0 : 30;

        return LocalTime.of(snapshot.getCollectedAt().getHour(), minute);
    }

    private static class BoardingAccumulator {

        private int total;
        private int count;
        private int fullSeatCount;

        private void add(int boarding) {
            total += boarding;
            count++;
        }

        private void addFullSeat() {
            fullSeatCount++;
        }

        private BigDecimal average() {
            return BigDecimal.valueOf(total)
                    .divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        }

        private BigDecimal fullSeatRate() {
            return BigDecimal.valueOf(fullSeatCount)
                    .divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
        }
    }
}
