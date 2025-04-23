package com.framework.gw.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

            Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

            getBodyAsBytes(requestBody);

            log.info("requestBody.log() : {}" , requestBody.log());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("LoggingFilter response execute.");
                log.info("response : {}", response.getStatusCode());
            }));
        };
    }

    @Data
    public static class Config {

    }

    public Mono<byte[]> getBodyAsBytes(Flux<DataBuffer> body) {
        return DataBufferUtils.join(body)
                .map(
                        dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            return bytes;
                        }
                );
    }

}
