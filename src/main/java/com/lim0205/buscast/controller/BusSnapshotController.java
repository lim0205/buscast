package com.lim0205.buscast.controller;

import com.lim0205.buscast.service.BusSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bus-snapshots")
@RequiredArgsConstructor
public class BusSnapshotController {

    private final BusSnapshotService busSnapshotService;

    @PostMapping("/collect")
    public void collect() {
        busSnapshotService.collectSnapshots();
    }
}