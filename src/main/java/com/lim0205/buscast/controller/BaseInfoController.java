package com.lim0205.buscast.controller;

import com.lim0205.buscast.dto.baseinfo.BaseInfoItem;
import com.lim0205.buscast.service.BaseInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/base-info")
public class BaseInfoController {

    private final BaseInfoService baseInfoService;

    @GetMapping
    public BaseInfoItem getBaseInfo() {
        return baseInfoService.getBaseInfo();
    }

    @GetMapping("/route-file")
    public String routeFile() {
        return baseInfoService.downloadRouteFile();
    }

    @PostMapping("/routes/import")
    public String importRoutes() {

        baseInfoService.importRoutes();

        return "Route Import Complete";
    }
}