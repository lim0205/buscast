package com.lim0205.buscast.repository;

import com.lim0205.buscast.entity.RouteStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RouteStationRepository extends JpaRepository<RouteStation, Long> {

    @Query("select distinct rs.stationId from RouteStation rs")
    List<Long> findAllStationIds();

    Optional<RouteStation> findFirstByRouteIdAndStationId(Long routeId, Long stationId);
}
