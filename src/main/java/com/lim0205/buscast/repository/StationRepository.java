package com.lim0205.buscast.repository;

import com.lim0205.buscast.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
}