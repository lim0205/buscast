package com.lim0205.buscast.statistics;

import java.math.BigDecimal;

public record QueueStatistics(
        BigDecimal avgQueue,
        int sampleCount
) {
}
