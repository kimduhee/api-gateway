package com.framework.gw.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            //TODO 로그적재를 위해 식별값과 함께 요청 값(body값 또는 parameter값)에 대해서 비동기 전송

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("request : {}", request.getId());
            log.info("request : {}", request.getQueryParams());
            log.info("request : {}", request.getBody());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("LoggingFilter response execute.");
                log.info("response : {}", response.getStatusCode());
            }));
        };
    }

    @Data
    public static class Config {
    }
}
