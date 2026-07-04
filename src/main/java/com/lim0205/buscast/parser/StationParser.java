package com.lim0205.buscast.parser;

import com.lim0205.buscast.entity.Station;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;

@Component
public class StationParser {

    public List<Station> parse(String txt) {

        List<Station> stations = new ArrayList<>();

        String[] rows = txt.split("\\^");

        // rows[0]은 헤더
        for (int i = 1; i < rows.length; i++) {

            if (rows[i].isBlank()) {
                continue;
            }

            String[] columns = rows[i].split("\\|", -1);

            Station station = Station.builder()
                    .stationId(parseLong(columns[0]))
                    .stationName(parseString(columns[2]))
                    .stationNo(parseInteger(columns[1]))
                    .latitude(parseDouble(columns[6]))
                    .longitude(parseDouble(columns[5]))
                    .region(parseString(columns[4]))
                    .centerYn(parseBoolean(columns[3]))
                    .build();

            stations.add(station);
        }

        return stations;
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

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Double.parseDouble(value.trim());
    }

    private String parseString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}