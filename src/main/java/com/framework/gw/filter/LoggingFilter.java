package com.framework.gw.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
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

            log.info("Request getMethod: {}",  request.getMethod().name());
            log.info("Request getId: {}",  request.getId());
            log.info("Request getSslInfo: {}",  request.getSslInfo());
            log.info("Request getURI: {}",  request.getURI());
            log.info("Request getBody: {}",  request.getBody());
            log.info("Request getQueryParams: {}",  request.getQueryParams());
            request.getHeaders().forEach((a,b) ->{
                log.info(a + "|" +b.get(0));
            });


            if (request.getMethod().name().equalsIgnoreCase("POST") || request.getMethod().name().equalsIgnoreCase("PUT")) {

                log.info("진입");

                request.getBody()
                        .flatMap(body -> {
                            log.info("진입: {}", body);
                            String requestBody = bufferToString(body);
                            log.info("Request body: {}",  requestBody);
                            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                                log.info("LoggingFilter response execute.1");
                                log.info("response1 : {}", response.getStatusCode());
                            }));
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

    private String bufferToString(DataBuffer dataBuffer) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        return new String(bytes);
    }
}
