package com.lim0205.buscast.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lim0205.buscast.dto.common.ResponseHeader;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusLocationResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @NoArgsConstructor
    public static class Response {

        @JsonProperty("msgHeader")
        private ResponseHeader msgHeader;

        @JsonProperty("msgBody")
        private BusLocationBody msgBody;
    }
}