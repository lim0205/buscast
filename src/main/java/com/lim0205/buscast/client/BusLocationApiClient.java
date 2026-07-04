package com.lim0205.buscast.client;

import com.lim0205.buscast.dto.location.BusLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BusLocationApiClient {

    private final RestClient restClient;

    @Value("${external.bus.key}")
    private String serviceKey;

    public BusLocationResponse getBusLocations(Long routeId) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/buslocationservice/v2/getBusLocationListv2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("routeId", routeId)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(BusLocationResponse.class);
    }
}