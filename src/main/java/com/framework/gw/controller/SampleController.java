package com.framework.gw.controller;

import com.framework.gw.entity.InterfaceInfoEntity;
import com.framework.gw.service.InterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final InterfaceService interfaceService;

    @PostMapping("/sample")
    public Mono<InterfaceInfoEntity> sample() {
        System.out.println("들어옴!!");
        return interfaceService.interfaceList("com.framework.dto.Category","cateId");
    }
}
