package com.lim0205.buscast.scheduler;

import com.lim0205.buscast.service.BusSnapshotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotScheduler {

    private final BusSnapshotService busSnapshotService;

    @Scheduled(fixedDelay = 180000)
    public void collect() {

        log.info("=== Snapshot 수집 시작 ===");

        try {
            busSnapshotService.collectSnapshots();
        } catch (Exception e) {
            log.error("Snapshot 수집 실패", e);
        }

        log.info("=== Snapshot 수집 종료 ===");
    }
}