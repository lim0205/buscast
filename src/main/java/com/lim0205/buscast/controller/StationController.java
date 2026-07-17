package com.lim0205.buscast.controller;

import com.lim0205.buscast.service.StationService;
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
@RequestMapping("/stations")
@Tag(name = "Station", description = "정류장 데이터 관리")
public class StationController {

    private final StationService stationService;
    @Operation(
            summary = "정류장 정보 저장",
            description = """
                공공데이터포털에서 정류장 정보를 다운로드하여
                파싱 후 station 테이블에 저장합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정류장 데이터 저장 완료")
    })
    @PostMapping("/init")
    public void init() {
        stationService.saveStations();
    }
}