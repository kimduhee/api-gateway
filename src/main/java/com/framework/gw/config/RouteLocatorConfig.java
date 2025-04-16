package com.framework.gw.config;

import com.framework.gw.entity.RouteEntity;
import com.framework.gw.filter.LoggingFilter;
import com.framework.gw.repository.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class RouteLocatorConfig {

    private final RouteRepository routeRepository;
    private final LoggingFilter loggingFilter;

    public RouteLocatorConfig(RouteRepository routeRepository, LoggingFilter loggingFilter) {
        this.routeRepository = routeRepository;
        this.loggingFilter = loggingFilter;
    }

    /**
     * DB 정보로 Route 초기 설정
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator dynamicRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        List<RouteEntity> routeList = routeRepository.findAll();

        if(log.isInfoEnabled()) {
            log.info("----------------------------------");
            log.info("- Route info");
        }

        for (RouteEntity route : routeList) {
            if(log.isInfoEnabled()) {
                log.info("- route id:[{}], path:[{}], uri:[{}]", route.getRouteId(), route.getPath(), route.getUri());
            }

            routes.route(route.getRouteId(),
                    r -> r
                            .order(-1)
                            .path(route.getPath())
                            .filters(f -> f
                                    .addRequestHeader("request", "request-header")
                                    .addResponseHeader("response", "response-header")
                                    .filter(loggingFilter.apply(new LoggingFilter.Config())))
                            .uri(route.getUri()));
        }

        if(log.isInfoEnabled()) {
            log.info("----------------------------------");
        }

        return routes.build();
    }
}
