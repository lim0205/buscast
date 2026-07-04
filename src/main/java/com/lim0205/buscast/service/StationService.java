package com.lim0205.buscast.service;

import com.lim0205.buscast.client.BaseInfoApiClient;
import com.lim0205.buscast.client.DownloadClient;
import com.lim0205.buscast.entity.Station;
import com.lim0205.buscast.repository.RouteStationRepository;
import com.lim0205.buscast.repository.StationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lim0205.buscast.parser.StationParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StationService {

    private final BaseInfoApiClient baseInfoApiClient;
    private final DownloadClient downloadClient;
    private final StationParser stationParser;
    private final StationRepository stationRepository;
    private final RouteStationRepository routeStationRepository;

    @Transactional
    public void saveStations() {

        String stationUrl = baseInfoApiClient.getBaseInfo()
                .getResponse()
                .getMsgBody()
                .getBaseInfoItem()
                .getStationDownloadUrl();

        String txt = downloadClient.download(stationUrl);

// 오늘 파일이 없으면 전날 파일 사용
        if (txt == null) {

            String today = LocalDate.now()
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            String yesterday = LocalDate.now()
                    .minusDays(1)
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            String yesterdayUrl = stationUrl.replace(today, yesterday);

            txt = downloadClient.download(yesterdayUrl);
        }

        if (txt == null) {
            throw new IllegalStateException("정류장 다운로드 실패");
        }

        List<Station> stations = stationParser.parse(txt);

// RouteStation에서 사용하는 stationId만 가져오기
        Set<Long> stationIds = new HashSet<>(routeStationRepository.findAllStationIds());

// 사용하지 않는 정류장 제거
        stations.removeIf(station -> !stationIds.contains(station.getStationId()));

        stationRepository.deleteAllInBatch();
        stationRepository.saveAll(stations);
    }
}