package com.lim0205.buscast.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusLocationItem {

    @JsonProperty("vehId")
    private Long vehId;

    @JsonProperty("plateNo")
    private String plateNo;

    @JsonProperty("routeId")
    private Long routeId;

    @JsonProperty("stationId")
    private Long stationId;

    @JsonProperty("stationSeq")
    private Integer stationSeq;

    @JsonProperty("remainSeatCnt")
    private Integer remainSeatCnt;

    @JsonProperty("stateCd")
    private Short stateCd;
}