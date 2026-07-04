package com.lim0205.buscast.parser;

import com.lim0205.buscast.entity.Route;
import org.springframework.stereotype.Component;

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

            // 특수 노선 제외
            if (routeName != null &&
                    (routeName.contains("출근")
                            || routeName.contains("퇴근")
                            || routeName.contains("예약")
                            || routeName.contains("콜"))) {
                continue;
            }

            Route route = Route.builder()
                    .routeId(parseLong(columns[0]))
                    .routeName(parseString(columns[5]))
                    .routeTypeCd(parseInteger(columns[6]))
                    .startStationId(parseLong(columns[7]))
                    .endStationId(parseLong(columns[2]))

                    .upFirstTime(parseString(columns[13]))
                    .upLastTime(parseString(columns[14]))
                    .downFirstTime(parseString(columns[15]))
                    .downLastTime(parseString(columns[16]))
                    .peakAlloc(parseInteger(columns[17]))
                    .nonPeakAlloc(parseInteger(columns[18]))

                    .satUpFirstTime(parseString(columns[19]))
                    .satUpLastTime(parseString(columns[20]))
                    .satDownFirstTime(parseString(columns[21]))
                    .satDownLastTime(parseString(columns[22]))
                    .satPeakAlloc(parseInteger(columns[23]))
                    .satNonPeakAlloc(parseInteger(columns[24]))

                    .sunUpFirstTime(parseString(columns[25]))
                    .sunUpLastTime(parseString(columns[26]))
                    .sunDownFirstTime(parseString(columns[27]))
                    .sunDownLastTime(parseString(columns[28]))
                    .sunPeakAlloc(parseInteger(columns[29]))
                    .sunNonPeakAlloc(parseInteger(columns[30]))
                    .build();

            routes.add(route);
        }
        System.out.println("rows.length = " + rows.length);
        System.out.println("routes.size = " + routes.size());

        return routes;
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