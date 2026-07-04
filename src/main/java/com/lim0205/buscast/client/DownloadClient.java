package com.lim0205.buscast.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class DownloadClient {

    private final RestClient restClient;

    public String download(String downloadUrl) {
        try {
            return restClient.get()
                    .uri(downloadUrl)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return null;
        }
    }
}