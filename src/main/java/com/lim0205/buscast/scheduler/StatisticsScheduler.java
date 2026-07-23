package com.lim0205.buscast.scheduler;

import com.lim0205.buscast.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticsScheduler {

    private final StatisticsService statisticsService;

    @Scheduled(cron = "0 0 3 * * *")
    public void calculateStatistics() {


        log.info("=== Statistics batch started ===");

        try {
            statisticsService.calculateStatistics();
            log.info("=== Statistics batch finished ===");
        } catch (Exception e) {
            log.error("=== Statistics batch failed ===", e);
        }
    }
}