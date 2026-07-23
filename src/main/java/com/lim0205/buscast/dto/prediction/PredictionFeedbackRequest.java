package com.lim0205.buscast.dto.prediction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PredictionFeedbackRequest(
        @Min(0)
        @Schema(description = "사용자가 직접 입력한 사용자 앞의 대기 인원", example = "5")
        Integer userQueue,

        @Min(0)
        @Schema(description = "사용자가 직접 입력한 해당 노선, 해당 정류장의 전체 대기 인원", example = "7")
        Integer actualQueue,

        @NotNull
        @Schema(description = "실제 승차 성공 여부(true: 승차 성공, false: 승차 실패)", example = "true")
        Boolean boardResult
) {
}
