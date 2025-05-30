package com.framework.gw.repository;

import com.framework.gw.entity.InterfaceInfoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InterfaceInfoRepository extends ReactiveCrudRepository<InterfaceInfoEntity, String> {

    @Query("SELECT * FROM INTERFACE_INFO WHERE PACKAGE_NAME= :packageName AND ORG_NAME= :orgName")
    Mono<InterfaceInfoEntity> findByPackageNameAndOrgName(String packageName, String orgName);
}
