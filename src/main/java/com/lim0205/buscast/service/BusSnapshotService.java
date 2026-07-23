package com.lim0205.buscast.service;

import com.lim0205.buscast.client.BusLocationApiClient;
import com.lim0205.buscast.dto.location.BusLocationItem;
import com.lim0205.buscast.dto.location.BusLocationResponse;
import com.lim0205.buscast.entity.BusSnapshot;
import com.lim0205.buscast.entity.Route;
import com.lim0205.buscast.repository.BusSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusSnapshotService {

    private final BusLocationApiClient busLocationApiClient;
    private final RouteService routeService;
    private final BusSnapshotRepository busSnapshotRepository;

    @Transactional
    public void collectSnapshots() {

        List<Route> routes = routeService.findRunningRoutes();
        List<BusSnapshot> snapshots = new ArrayList<>();

        LocalDateTime collectedAt = LocalDateTime.now();
        log.info("Zone = {}", ZoneId.systemDefault());
        log.info("CollectedAt = {}", collectedAt);

        for (Route route : routes) {

            try {

                BusLocationResponse response =
                        busLocationApiClient.getBusLocations(route.getRouteId());

                if (response == null
                        || response.getResponse() == null
                        || response.getResponse().getMsgBody() == null
                        || response.getResponse().getMsgBody().getBusLocationList() == null) {
                    continue;
                }

                List<BusLocationItem> items =
                        response.getResponse().getMsgBody().getBusLocationList();

                for (BusLocationItem item : items) {

                    BusSnapshot snapshot = BusSnapshot.builder()
                            .collectedAt(collectedAt)
                            .vehId(item.getVehId())
                            .plateNo(item.getPlateNo())
                            .routeId(item.getRouteId())
                            .stationId(item.getStationId())
                            .stationSeq(item.getStationSeq())
                            .remainSeatCnt(item.getRemainSeatCnt())
                            .stateCd(item.getStateCd())
                            .build();

                    snapshots.add(snapshot);
                }

            } catch (Exception e) {
                log.error("노선 {} ({}) 수집 실패",
                        route.getRouteName(),
                        route.getRouteId(),
                        e);
            }
        }
        if (!snapshots.isEmpty()) {
            busSnapshotRepository.saveAll(snapshots);
        }
    }
}