package com.framework.gw.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.ConvertCaseStrategy());
        //objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

        objectMapper.registerModule(new Jdk8Module());
        // 8버전 이후에 나온 클래스들을 처리 해주기 위해서 (Optional같은) 모듈 등록

        objectMapper.registerModule(new JavaTimeModule());
        // local date같은 애들 처리 모듈 등록

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 모르는 json field가 들어오더라도 익셉션 무시하고 객체에 파싱

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 비어있는 객체 json으로 직렬화시 익셉션 무시 그냥 비어있는 json 객체 만듦


        // 날짜 관련 타임스탬프 직렬화 disable -> ISO-8601 형태로 포맷되어 나온다
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
