package com.lim0205.buscast.controller;

import com.lim0205.buscast.dto.prediction.PredictionFeedbackRequest;
import com.lim0205.buscast.dto.prediction.PredictionResult;
import com.lim0205.buscast.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/predictions")
@Tag(name = "Prediction", description = "최종 승차 확률 예측")
public class PredictionController {

    private final PredictionService predictionService;

    @GetMapping("/boarding")
    @Operation(
            summary = "승차 확률 예측",
            description = """
                노선과 정류장,사용자가 입력한 대기인원을 이용하여 승차 확률을 계산.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "승차 확률 예측 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "노선 또는 정류장을 찾을 수 없음")
    })
    public PredictionResult predictBoarding(
            @Parameter(
                    description = "광역버스 노선 ID",
                    example = "200000112",
                    required = true
            )
            @RequestParam Long routeId,
            @Parameter(
                    description = "정류장 ID",
                    example = "228000123",
                    required = true
            )
            @RequestParam Long stationId,
            @Parameter(
                    description = "직접 입력한 사용자 앞의 대기 인원 (선택)",
                    example = "5"
            )
            @RequestParam(required = false) Integer userQueue,
            @Parameter(
                    description = "예측 기준 시각 (생략 시 현재 시간)",
                    example = "2026-07-18T08:30:00"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime targetTime
    ) {
        return predictionService.predict(
                routeId,
                stationId,
                userQueue,
                targetTime
        );
    }

    @PostMapping("/{id}/feedback")
    @Operation(
            summary = "예측 결과 피드백",
            description = "사용자가 실제 승차 결과와 대기 인원을 전송하여 예측 정확도를 향상시킵니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "예측 결과를 찾을 수 없음")
    })
    public void feedback(
            @Parameter(
                    description = "예측 결과 ID",
                    example = "15",
                    required = true
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자의 실제 승차 결과",
                    required = true
            )
            @Valid @RequestBody PredictionFeedbackRequest request
    ) {
        predictionService.updateFeedback(id, request);
    }
}
