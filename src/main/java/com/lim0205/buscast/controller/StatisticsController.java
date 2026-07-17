package com.lim0205.buscast.controller;

import com.lim0205.buscast.service.StatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@Tag(name = "Statistics", description = "통계 계산")
public class StatisticsController {

    private final StatisticsService statisticsService;
    @Operation(
            summary = "통계 계산",
            description = """
                수집된 버스 스냅샷 데이터를 기반으로
                시간대별 승차 통계와 승차 성공 확률을 계산하여
                station_statistics 테이블을 갱신합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "통계 계산 완료")
    })
    @PostMapping("/calculate")
    public void calculate() {
        statisticsService.calculateStatistics();
    }
}