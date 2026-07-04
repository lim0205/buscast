package com.lim0205.buscast.parser;

import com.lim0205.buscast.entity.RouteStation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RouteStationParser {

    public List<RouteStation> parse(String txt) {

        List<RouteStation> routeStations = new ArrayList<>();

        String[] rows = txt.split("\\^");

        // rows[0]은 헤더
        for (int i = 1; i < rows.length; i++) {

            if (rows[i].isBlank()) {
                continue;
            }

            String[] columns = rows[i].split("\\|", -1);

            RouteStation routeStation = RouteStation.builder()
                    .routeId(parseLong(columns[0]))        // routeId
                    .upDown(parseString(columns[2]))       // 상행 / 하행
                    .stationSeq(parseInteger(columns[3]))  // 정류장 순서
                    .stationId(parseLong(columns[4]))      // stationId
                    .build();

            routeStations.add(routeStation);
        }

        return routeStations;
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.parseLong(value.trim());
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.parseInt(value.trim());
    }

    private String parseString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}