package com.lim0205.buscast.controller;

import com.lim0205.buscast.service.BusSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/bus-snapshots")
@RequiredArgsConstructor
@Tag(
        name = "Bus Snapshot",
        description = "실시간 버스 운행 정보 수집 API"
)
public class BusSnapshotController {

    private final BusSnapshotService busSnapshotService;
    @Operation(
            summary = "버스 스냅샷 수집",
            description = """
                공공데이터 API를 호출하여
                실시간 버스 위치, 잔여 좌석, 혼잡도 등의 정보를 수집하고
                bus_snapshot 테이블에 저장합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수집 완료")
    })
    @PostMapping("/collect")
    public void collect() {
        busSnapshotService.collectSnapshots();
    }
}