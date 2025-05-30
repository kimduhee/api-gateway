package com.framework.gw.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.framework.dto.Category;
import com.framework.gw.entity.RouteInfo;
import com.framework.gw.filter.JwtValidFilter;
import com.framework.gw.filter.LoggingFilter;
import com.framework.gw.repository.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.factory.CacheRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
//@EnableR2dbcRepositories
//@EnableR2dbcAuditing
public class RouteLocatorConfig {

    private final RouteRepository routeRepository;
    private final LoggingFilter loggingFilter;
    private final JwtValidFilter jwtValidFilter;
    private final CacheRequestBodyGatewayFilterFactory cacheRequestBodyGatewayFilterFactory;
    private final ObjectMapper objectMapper;

    public RouteLocatorConfig(RouteRepository routeRepository,
                              LoggingFilter loggingFilter,
                              JwtValidFilter jwtValidFilter,
                              CacheRequestBodyGatewayFilterFactory cacheRequestBodyGatewayFilterFactory, ObjectMapper objectMapper) {
        this.routeRepository = routeRepository;
        this.loggingFilter = loggingFilter;
        this.jwtValidFilter = jwtValidFilter;
        this.cacheRequestBodyGatewayFilterFactory = cacheRequestBodyGatewayFilterFactory;
        this.objectMapper = objectMapper;
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
                                    .modifyResponseBody(String.class, String.class,
                                            (exchange, s) -> Mono.just(responseStr(s, exchange.getResponse().getHeaders().get("package").get(0))))
                                    .modifyRequestBody(String.class, String.class,
                                            (exchange, s) -> Mono.just(requestStr(s)))
                                    //나열한 순서로 filter 실행
                                    // CacheRequestBodyGatewayFilterFactory => body 값을 캐싱하기 위한 필터 적용
                                    //.filters(cacheRequestBodyGatewayFilterFactory.apply(new CacheRequestBodyGatewayFilterFactory.Config())
                                            //, loggingFilter.apply(new LoggingFilter.Config())
                                            //, jwtValidFilter.apply(new JwtValidFilter.Config())))
                            .filters(//cacheRequestBodyGatewayFilterFactory.apply(new CacheRequestBodyGatewayFilterFactory.Config())
                                    //, loggingFilter.apply(new LoggingFilter.Config())
                                    jwtValidFilter.apply(new JwtValidFilter.Config())))
                            .uri(route.getUri()));
        }

        return routes.build();
    }

    /**
     * request value
     * @param requestStr
     * @return
     */
    private String requestStr(String requestStr) {
        log.info(requestStr);
        return requestStr;
    }

    /**
     * response value
     * @param responseStr
     * @return
     */
    private String responseStr(String responseStr, String className) {

        log.info("response orgin : {}", responseStr);
        log.info("response path : {}", className);

        try {
            ObjectMapper obj = objectMapper;
            obj.setPropertyNamingStrategy(new PropertyNamingStrategies.ConvertCaseStrategy());
            Object returnObj = obj.readValue(responseStr, Class.forName(className));
            responseStr = obj.writeValueAsString(returnObj);
            log.info("testText : {}", responseStr);
        } catch(Exception e) {
            log.info("Exception : {}", e);
        }

/*
        List<String> dbData = new ArrayList<>();
        dbData.add("1|cateId:카테고리ID");
        dbData.add("1|cateNm:카테고리명");
        dbData.add("1|goodsList:상품목록");
        dbData.add("2|goodsList:상품목록|goodsId:상품ID,goodsNm:상품명,goodsPrice:상품가격");

        String returnStr = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> orgMap = objectMapper.readValue(responseStr, new TypeReference<>() {});

            Map<String, Object> newMap = new HashMap<>();
            String stepNo = "1";
            for(String convStr : dbData) {
                String[] convSplit = convStr.split("\\|");
                if("1".equals(convSplit[0].toString())) {
                    String[] mappingStr = convSplit[1].toString().split(":");
                    newMap.put(mappingStr[1], orgMap.get(mappingStr[0]));
                } else {
                    convDataMap(convSplit[0].toString(), );
                }

                stepNo = convSplit[0].toString();
            }

            //newMap.put("카테고리ID", orgMap.get("cateId"));
            //newMap.put("카테고리명", orgMap.get("cateNm"));
            //newMap.put("상품목록", orgMap.get("goodsList"));

            if(newMap.get("상품목록") instanceof ArrayList) {
                List li = (List)newMap.get("상품목록");
                List newList = new ArrayList<>();
                Map<String, Object> mp = null;
                for(int i=0;i<li.size();i++) {
                    mp = (Map)li.get(i);
                    Map<String, Object> listMap = new HashMap<>();
                    listMap.put("상품ID", mp.get("goodsId"));
                    listMap.put("상품명", mp.get("goodsNm"));
                    listMap.put("상품가격", mp.get("goodsPrice"));
                    newList.add(listMap);
                    log.info("Map: {}", mp);
                }
                newMap.put("상품목록", newList);
            }

            returnStr = objectMapper.writeValueAsString(newMap);
        } catch(Exception e) {
        }

        log.info("response new : {}", returnStr);
*/
//        return returnStr;
        return responseStr;
    }

    private Map<String, Object> convDataMap(String step, Map<String, Object> orgMap) {

        Map<String, Object> returnMap = new HashMap<>();

        return returnMap;
    }
}
