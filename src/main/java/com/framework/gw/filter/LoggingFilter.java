package com.framework.gw.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Request getMethod: {}",  request.getMethod().name());
            log.info("Request getId: {}",  request.getId());
            log.info("Request getURI: {}",  request.getURI());
            log.info("Request getQueryParams: {}",  request.getQueryParams());
            //request.getHeaders().forEach((a,b) ->{
            //    log.info(a + ": " +b.get(0));
            //});

            //POST 일 경우에만 body값 체크
            if (request.getMethod().name().equalsIgnoreCase("POST")) {
                request.getBody()
                    .subscribe(
                        dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            String body = new String(bytes, StandardCharsets.UTF_8);
                            log.info("request body : {}", body);
                        },
                        error -> {
                            log.info("에러!!");
                        },
                        () -> {
                            log.info("완료!!");
                        });
            }

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
