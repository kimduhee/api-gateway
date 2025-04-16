package com.framework.gw.filter;

import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtValidFilter extends AbstractGatewayFilterFactory<JwtValidFilter.Config> {

    public JwtValidFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(JwtValidFilter.Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            //TODO redis에서 요청uri에 대해 컨텍스트 path에 관련된 서비스 uri 조회
            //TODO 해당 path 없을 경우 DB 조회

            boolean isLoginRequire = false;

            //TODO 조회결과에 대해 로그인이 필요한 서비스일 경우 isLoginRequire = true로 변경
            isLoginRequire = true;
            //If the service requires login, check JWT
            if(isLoginRequire) {
                if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Empty JWT", HttpStatus.UNAUTHORIZED);
                }

                String authorizationStr = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String jwtStr = authorizationStr.replace("Bearer ", "");
                if(!jwtValid(jwtStr)) {
                    return onError(exchange, "JWT is not valid", HttpStatus.UNAUTHORIZED);
                }
            }

            ServerHttpResponse response = exchange.getResponse();

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("JwtValidFilter response execute.");
                log.info("response : {}", response.getStatusCode());
            }));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    /**
     * JWT valid check
     *
     * @param jwtStr JWT String
     * @return
     */
    private boolean jwtValid(String jwtStr) {
        //TODO 식별번호로 변경해야함(사용자번호)
        String subject = null;
        try {
           subject = Jwts.parser().setSigningKey("test")
                   .parseClaimsJws(jwtStr).getBody()
                   .getSubject();
        } catch(Exception e) {
            log.error("JWT parse fail!");
        }
        return Strings.isBlank(subject);
    }

    @Data
    public static class Config {
    }
}
