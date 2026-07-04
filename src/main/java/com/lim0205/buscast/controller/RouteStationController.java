package com.lim0205.buscast.controller;

import com.lim0205.buscast.service.RouteStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route-stations")
@RequiredArgsConstructor
public class RouteStationController {

    private final RouteStationService routeStationService;

    @PostMapping("/init")
    public void init() {
        routeStationService.saveRouteStations();
    }
}
