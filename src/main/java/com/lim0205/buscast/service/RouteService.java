package com.lim0205.buscast.service;

import com.lim0205.buscast.entity.Route;
import com.lim0205.buscast.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RouteService {

    private static final Set<String> SUPPORTED_ROUTES = Set.of(
            "G6000",
            "M4137",
            "M4130",
            "M5107",
            "G6100",
            "M2323",
            "M5121",
            "G6009",
            "M4434",
            "G1300",
            "M6427",
            "M4108",
            "M4101",
            "M4403",
            "G2100",
            "G1200",
            "M2316",
            "M7111",
            "M4102",
            "M5333",
            "G8110",
            "M5556",
            "G6003",
            "G7426",
            "M5422",
            "G3202",
            "G1003"
    );

    private final RouteRepository routeRepository;

    public List<Route> findRunningRoutes() {

        LocalTime now = LocalTime.now();

        return routeRepository.findAll()
                .stream()
                .filter(route -> SUPPORTED_ROUTES.contains(route.getRouteName()))
                .filter(route -> isRunning(route, now))
                .toList();
    }

    private boolean isRunning(Route route, LocalTime now) {

        return isRunningDirection(route.getUpFirstTime(), route.getUpLastTime(), now)
                || isRunningDirection(route.getDownFirstTime(), route.getDownLastTime(), now);
    }

    private static final long LAST_BUS_BUFFER_MINUTES = 50;

    private boolean isRunningDirection(LocalTime first, LocalTime last, LocalTime now) {

        if (first == null || last == null) {
            return false;
        }

        LocalTime end = last.plusMinutes(LAST_BUS_BUFFER_MINUTES);

        if (end.isAfter(first)) {
            return !now.isBefore(first) && !now.isAfter(end);
        }

        return !now.isBefore(first) || !now.isAfter(end);
    }
}