package com.lim0205.buscast.service;

import com.lim0205.buscast.client.BaseInfoApiClient;
import com.lim0205.buscast.client.DownloadClient;
import com.lim0205.buscast.entity.RouteStation;
import com.lim0205.buscast.repository.RouteRepository;
import com.lim0205.buscast.repository.RouteStationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lim0205.buscast.parser.RouteStationParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RouteStationService {

    private final BaseInfoApiClient baseInfoApiClient;
    private final DownloadClient downloadClient;
    private final RouteStationParser routeStationParser;
    private final RouteStationRepository routeStationRepository;
    private final RouteRepository routeRepository;

    @Transactional
    public void saveRouteStations() {

        String routeStationUrl = baseInfoApiClient.getBaseInfo()
                .getResponse()
                .getMsgBody()
                .getBaseInfoItem()
                .getRouteStationDownloadUrl();

        String txt = downloadClient.download(routeStationUrl);

// 오늘 파일이 없으면 전날 파일로 재시도
        if (txt == null) {

            String today = LocalDate.now()
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            String yesterday = LocalDate.now()
                    .minusDays(1)
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            String yesterdayUrl = routeStationUrl.replace(today, yesterday);

            txt = downloadClient.download(yesterdayUrl);
        }

        if (txt == null) {
            throw new IllegalStateException("노선-정류장 다운로드 실패");
        }

        List<RouteStation> routeStations = routeStationParser.parse(txt);

        Set<Long> routeIds = new HashSet<>(routeRepository.findAllRouteIds());

        routeStations.removeIf(rs -> !routeIds.contains(rs.getRouteId()));

        routeStationRepository.deleteAllInBatch();
        routeStationRepository.saveAll(routeStations);
    }
}