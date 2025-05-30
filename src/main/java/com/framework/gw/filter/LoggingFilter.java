package com.framework.gw.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            ServerWebExchange originalExchange = exchange;
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            ServerHttpResponse originalResponse = originalExchange.getResponse();

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

            return chain.filter(originalExchange.mutate().response(
                    new ServerHttpResponseDecorator(originalResponse) {
                        @Override
                        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                            log.info("response body: {}", body);
                            ObjectMapper om = new ObjectMapper();
                            Map<String, String> body1 = Map.of("message", "Unauthorized", "code", "401", "success", "false");

                            DataBuffer buffer = null;

                            try {
                                buffer = response.bufferFactory().wrap(om.writeValueAsBytes(body1));

                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                            return super.writeWith(Mono.just(buffer))
                                    .doOnError(throwable -> {
                                        // 에러 발생 시 처리
                                        System.err.println("Error while writing modified response: " + throwable.getMessage());
                                        // 에러 로그 기록 및 처리 (예: 다른 에러 핸들링 로직 수행)
                                    });
                        }

                        @Override
                        public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                            log.info("response body: {}", body);
                            return super.writeAndFlushWith(body);
                        }

                        @Override
                        public DataBufferFactory bufferFactory() {
                            log.info("response body: {}", this.getDelegate());
                            return super.getDelegate().bufferFactory();
                        }

                    }).build()
            );
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                log.info("LoggingFilter response execute.");
//                log.info("response : {}", response.getStatusCode());
//            }));
        };
    }

    @Data
    public static class Config {

    }
}
