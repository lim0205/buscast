package com.lim0205.buscast.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BusLocationBody {

    @JsonProperty("busLocationList")
    private List<BusLocationItem> busLocationList;
}