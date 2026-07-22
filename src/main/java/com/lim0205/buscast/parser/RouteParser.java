package com.lim0205.buscast.parser;

import com.lim0205.buscast.entity.Route;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RouteParser {

    public List<Route> parse(String txt) {

        List<Route> routes = new ArrayList<>();

        String[] rows = txt.split("\\^");

        // rows[0]은 헤더
        for (int i = 1; i < rows.length; i++) {

            if (rows[i].isBlank()) {
                continue;
            }

            String[] columns = rows[i].split("\\|", -1);

            String routeName = parseString(columns[5]);
            Integer routeTypeCd = parseInteger(columns[6]);

            // 11(직행좌석), 14(M버스), 21(직행좌석 농어촌)만 저장
            if (routeTypeCd == null ||
                    (routeTypeCd != 11 &&
                            routeTypeCd != 14 &&
                            routeTypeCd != 21)) {
                continue;
            }

            // M버스, G버스만 저장 (예약 노선 제외)
            if (routeName == null
                    || (!routeName.startsWith("M") && !routeName.startsWith("G"))
                    || routeName.contains("예약")
                    || routeName.contains("N")) {
                continue;
            }

            Route route = Route.builder()
                    .routeId(parseLong(columns[0]))
                    .routeName(parseString(columns[5]))
                    .routeTypeCd(parseInteger(columns[6]))
                    .startStationId(parseLong(columns[7]))
                    .endStationId(parseLong(columns[2]))

                    .upFirstTime(parseTime(columns[13]))
                    .upLastTime(parseTime(columns[14]))
                    .downFirstTime(parseTime(columns[15]))
                    .downLastTime(parseTime(columns[16]))

                    .peakAlloc(parseInteger(columns[17]))
                    .nonPeakAlloc(parseInteger(columns[18]))

                    .satUpFirstTime(parseTime(columns[19]))
                    .satUpLastTime(parseTime(columns[20]))
                    .satDownFirstTime(parseTime(columns[21]))
                    .satDownLastTime(parseTime(columns[22]))

                    .satPeakAlloc(parseInteger(columns[23]))
                    .satNonPeakAlloc(parseInteger(columns[24]))

                    .sunUpFirstTime(parseTime(columns[25]))
                    .sunUpLastTime(parseTime(columns[26]))
                    .sunDownFirstTime(parseTime(columns[27]))
                    .sunDownLastTime(parseTime(columns[28]))

                    .sunPeakAlloc(parseInteger(columns[29]))
                    .sunNonPeakAlloc(parseInteger(columns[30]))
                    .build();

            routes.add(route);
        }
        System.out.println("rows.length = " + rows.length);
        System.out.println("routes.size = " + routes.size());

        return routes;
    }

    private LocalTime parseTime(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalTime.parse(value.trim());
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.parseInt(value.trim());
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.parseLong(value.trim());
    }

    private String parseString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

}