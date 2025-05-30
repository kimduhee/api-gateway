package com.framework.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class GwApplication {

	public static void main(String[] args) {
		SpringApplication.run(GwApplication.class, args);
		//Zipkin trace ID를 위해
		//ThreadLocal에 대해 전역적인 자동 Context 전파가 가능하도록 함
		Hooks.enableAutomaticContextPropagation();
	}

}
