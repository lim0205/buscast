package com.lim0205.buscast.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseHeader {

    @JsonProperty("queryTime")
    private String queryTime;

    @JsonProperty("resultCode")
    private Integer resultCode;

    @JsonProperty("resultMessage")
    private String resultMessage;
}
