package com.lim0205.buscast.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class DownloadService {

    private final RestClient restClient;

    public String download(String downloadUrl) {

        return restClient.get()
                .uri(downloadUrl)
                .retrieve()
                .body(String.class);
    }
}