package com.lim0205.buscast.repository;

import com.lim0205.buscast.entity.StationStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationStatisticsRepository extends JpaRepository<StationStatistics, Long> {
}