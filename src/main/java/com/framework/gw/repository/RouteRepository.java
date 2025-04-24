package com.framework.gw.repository;

import com.framework.gw.entity.RouteInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends ReactiveCrudRepository<RouteInfo, String> {
}
