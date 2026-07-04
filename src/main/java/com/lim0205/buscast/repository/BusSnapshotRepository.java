package com.lim0205.buscast.repository;

import com.lim0205.buscast.entity.BusSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusSnapshotRepository extends JpaRepository<BusSnapshot, Long> {
    @Query("select r.routeId from Route r")
    List<Long> findAllRouteIds();
}