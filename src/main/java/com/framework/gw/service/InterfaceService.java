package com.framework.gw.service;

import com.framework.gw.entity.InterfaceInfoEntity;
import com.framework.gw.repository.InterfaceInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class InterfaceService {

    private final InterfaceInfoRepository interfaceInfoRepository;

    //@Transactional(readOnly = true)
    public Mono<InterfaceInfoEntity> interfaceList(String interfaceId, String orgName) {
        Mono<InterfaceInfoEntity> resultList = interfaceInfoRepository.findByPackageNameAndOrgName(interfaceId, orgName);
        return resultList;
    }
}
