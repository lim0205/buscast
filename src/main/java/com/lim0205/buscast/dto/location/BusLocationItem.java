package com.lim0205.buscast.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusLocationItem {

    @Schema(description = "차량 ID", example = "123456789")
    @JsonProperty("vehId")
    private Long vehId;

    @Schema(description = "차량 번호", example = "경기70바1234")
    @JsonProperty("plateNo")
    private String plateNo;

    @Schema(description = "노선 ID", example = "200000112")
    @JsonProperty("routeId")
    private Long routeId;

    @Schema(description = "현재 정류장 ID", example = "228000123")
    @JsonProperty("stationId")
    private Long stationId;

    @Schema(description = "노선 내 현재 정류장 순번", example = "18")
    @JsonProperty("stationSeq")
    private Integer stationSeq;

    @Schema(
            description = "잔여 좌석 수 (-1은 정보 없음)",
            example = "7"
    )
    @JsonProperty("remainSeatCnt")
    private Integer remainSeatCnt;

    @Schema(
            description = "운행 상태 코드",
            example = "2"
    )
    @JsonProperty("stateCd")
    private Short stateCd;
}