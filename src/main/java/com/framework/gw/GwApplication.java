package com.framework.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class GwApplication {

	public static void main(String[] args) {
		SpringApplication.run(GwApplication.class, args);
		//Zipkin trace ID를 위해
		//ThreadLocal에 대해 전역적인 자동 Context 전파가 가능하도록 함
		Hooks.enableAutomaticContextPropagation();
	}

}
