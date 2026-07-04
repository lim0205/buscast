package com.lim0205.buscast.repository;

import com.lim0205.buscast.entity.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long> {
}