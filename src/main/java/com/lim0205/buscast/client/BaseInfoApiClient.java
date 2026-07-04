package com.lim0205.buscast.client;

import com.lim0205.buscast.dto.baseinfo.BaseInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BaseInfoApiClient {

    private final RestClient restClient;

    @Value("${external.bus.key}")
    private String serviceKey;

    public BaseInfoResponse getBaseInfo() {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/baseinfoservice/v2/getBaseInfoItemv2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(BaseInfoResponse.class);
    }
}