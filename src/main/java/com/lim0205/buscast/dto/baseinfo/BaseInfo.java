package com.lim0205.buscast.dto.baseinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseInfo {

    private String routeDownloadUrl;

    private String stationDownloadUrl;

    private String routeStationDownloadUrl;

    private String vehicleDownloadUrl;
}