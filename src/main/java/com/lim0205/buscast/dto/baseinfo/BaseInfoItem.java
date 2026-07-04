package com.lim0205.buscast.dto.baseinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseInfoItem {

    @JsonProperty("routeDownloadUrl")
    private String routeDownloadUrl;

    @JsonProperty("routeVersion")
    private Integer routeVersion;

    @JsonProperty("stationDownloadUrl")
    private String stationDownloadUrl;

    @JsonProperty("stationVersion")
    private Integer stationVersion;

    @JsonProperty("routeStationDownloadUrl")
    private String routeStationDownloadUrl;

    @JsonProperty("routeStationVersion")
    private Integer routeStationVersion;

    @JsonProperty("vehicleDownloadUrl")
    private String vehicleDownloadUrl;

    @JsonProperty("vehicleVersion")
    private Integer vehicleVersion;
}