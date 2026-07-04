package com.lim0205.buscast.repository;

import com.lim0205.buscast.entity.BatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchHistoryRepository extends JpaRepository<BatchHistory, Long> {
}