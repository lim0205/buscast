package com.lim0205.buscast.statistics;

import java.math.BigDecimal;

public record BoardingStatistics(
        BigDecimal avgBoarding,
        int sampleCount,
        BigDecimal fullSeatRate
) {
}
