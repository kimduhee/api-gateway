package com.framework.gw.config;

import com.framework.gw.entity.RouteInfo;
import com.framework.gw.filter.JwtValidFilter;
import com.framework.gw.filter.LoggingFilter;
import com.framework.gw.repository.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
//@EnableR2dbcRepositories
//@EnableR2dbcAuditing
public class RouteLocatorConfig {

    private final RouteRepository routeRepository;
    private final LoggingFilter loggingFilter;
    private final JwtValidFilter jwtValidFilter;

    public RouteLocatorConfig(RouteRepository routeRepository,
                              LoggingFilter loggingFilter,
                              JwtValidFilter jwtValidFilter) {
        this.routeRepository = routeRepository;
        this.loggingFilter = loggingFilter;
        this.jwtValidFilter = jwtValidFilter;
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

        Flux<RouteInfo> routeList = routeRepository.findAll();

        if(log.isInfoEnabled()) {
            log.info("----------------------------------");
            log.info("- Route info");
        }

        for(RouteInfo route : routeList.toIterable()) {
            if(log.isInfoEnabled()) {
                log.info("- route id:[{}], path:[{}], uri:[{}]", route.getRouteId(), route.getPath(), route.getUri());
            }

            routes.route(route.getRouteId(),
                    r -> r
                            .path(route.getPath())
                            .filters(f -> f
                                    .addRequestHeader("request", "request-header")
                                    .addResponseHeader("response", "response-header")
                                    //나열한 순서로 filter 실행
//                                    .filters(loggingFilter.apply(new LoggingFilter.Config())
//                                            ,jwtValidFilter.apply(new JwtValidFilter.Config())))
                                    .filters(loggingFilter.apply(new LoggingFilter.Config())))
                            .uri(route.getUri()));
        }

        if(log.isInfoEnabled()) {
            log.info("----------------------------------");
        }

        return routes.build();
    }
}
