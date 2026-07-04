package com.lim0205.buscast.service;

import com.lim0205.buscast.client.BaseInfoApiClient;
import com.lim0205.buscast.dto.baseinfo.BaseInfoItem;
import com.lim0205.buscast.parser.RouteParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lim0205.buscast.entity.Route;
import com.lim0205.buscast.repository.RouteRepository;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseInfoService {

    private final BaseInfoApiClient baseInfoApiClient;

    public BaseInfoItem getBaseInfo() {

        return baseInfoApiClient
                .getBaseInfo()
                .getResponse()
                .getMsgBody()
                .getBaseInfoItem();
    }
    private final DownloadService downloadService;

    public String downloadRouteFile() {

        BaseInfoItem item = getBaseInfo();

        return downloadService.download(
                item.getRouteDownloadUrl()
        );
    }
    private final RouteParser routeParser;
    private final RouteRepository routeRepository;

    @Transactional
    public void importRoutes() {

        String txt = downloadRouteFile();

        List<Route> routes = routeParser.parse(txt);

        routeRepository.deleteAll();

        routeRepository.saveAll(routes);

        System.out.println(routes.size() + "개의 노선을 저장했습니다.");
    }
}
