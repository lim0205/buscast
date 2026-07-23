package com.lim0205.buscast.service;

import com.lim0205.buscast.entity.BatchHistory;
import com.lim0205.buscast.repository.BatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BatchHistoryService {

    public static final short STATUS_SUCCESS = 1;
    public static final short STATUS_FAILED = -1;

    private final BatchHistoryRepository batchHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(
            String jobName,
            LocalDateTime startedAt,
            Short status,
            Integer processedCount,
            String message
    ) {
        batchHistoryRepository.save(
                BatchHistory.builder()
                        .jobName(jobName)
                        .startedAt(startedAt)
                        .finishedAt(LocalDateTime.now())
                        .status(status)
                        .processedCount(processedCount)
                        .message(truncate(message))
                        .build()
        );
    }

    private String truncate(String message) {
        if (message == null || message.length() <= 255) {
            return message;
        }

        return message.substring(0, 255);
    }
}
