package com.lim0205.buscast.dto.baseinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lim0205.buscast.dto.common.ResponseHeader;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @NoArgsConstructor
    public static class Response {

        @JsonProperty("msgHeader")
        private ResponseHeader msgHeader;

        @JsonProperty("msgBody")
        private Body msgBody;
    }

    @Getter
    @NoArgsConstructor
    public static class Body {

        @JsonProperty("baseInfoItem")
        private BaseInfoItem baseInfoItem;
    }
}