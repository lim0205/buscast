package com.lim0205.buscast.controller;

import com.lim0205.buscast.dto.baseinfo.BaseInfoItem;
import com.lim0205.buscast.service.BaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/base-info")
@Tag(name = "Base Info", description = "공공데이터 기본 정보 API")
public class BaseInfoController {

    private final BaseInfoService baseInfoService;
    @Operation(
            summary = "공공데이터 기본 정보 조회",
            description = "공공데이터포털에서 제공하는 기본 정보와 다운로드 URL을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public BaseInfoItem getBaseInfo() {
        return baseInfoService.getBaseInfo();
    }

    @Operation(
            summary = "노선 파일 다운로드",
            description = "공공데이터포털에서 노선 정보를 다운로드하여 원본 데이터를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "다운로드 성공")
    })
    @GetMapping("/route-file")
    public String routeFile() {
        return baseInfoService.downloadRouteFile();
    }

    @Operation(
            summary = "노선 데이터 Import",
            description = "공공데이터포털의 노선 정보를 파싱하여 데이터베이스에 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Import 완료")
    })
    @PostMapping("/routes/import")
    public String importRoutes() {

        baseInfoService.importRoutes();

        return "Route Import Complete";
    }
}