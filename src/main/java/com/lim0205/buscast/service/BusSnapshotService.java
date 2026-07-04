package com.lim0205.buscast.service;

import com.lim0205.buscast.client.BusLocationApiClient;
import com.lim0205.buscast.dto.location.BusLocationItem;
import com.lim0205.buscast.dto.location.BusLocationResponse;
import com.lim0205.buscast.entity.BusSnapshot;
import com.lim0205.buscast.repository.BusSnapshotRepository;
import com.lim0205.buscast.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusSnapshotService {

    private final BusLocationApiClient busLocationApiClient;
    private final RouteRepository routeRepository;
    private final BusSnapshotRepository busSnapshotRepository;

    @Transactional
    public void collectSnapshots() {

        List<Long> routeIds = routeRepository.findAllRouteIds();

        List<BusSnapshot> snapshots = new ArrayList<>();

        LocalDateTime collectedAt = LocalDateTime.now();

        for (Long routeId : routeIds) {

            BusLocationResponse response =
                    busLocationApiClient.getBusLocations(routeId);

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
        }

        busSnapshotRepository.saveAll(snapshots);
    }
}