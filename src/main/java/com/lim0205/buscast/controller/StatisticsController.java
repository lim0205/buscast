package com.lim0205.buscast.controller;

import com.lim0205.buscast.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/calculate")
    public void calculate() {
        statisticsService.calculateStatistics();
    }
}