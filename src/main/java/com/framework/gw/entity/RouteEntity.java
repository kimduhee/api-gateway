package com.framework.gw.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="ROUTE_INFO")
@Entity
public class RouteEntity {

    @Id
    @Column(name="ROUTE_ID", nullable = false)
    private String routeId;
    @Column(name="PATH", nullable = false)
    private String path;
    @Column(name="URI", nullable = false)
    private String uri;
}
