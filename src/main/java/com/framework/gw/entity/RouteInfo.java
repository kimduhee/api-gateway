package com.framework.gw.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
//import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Table(name="ROUTE_INFO")
//public class RouteInfo implements Persistable<String> {
public class RouteInfo {
    @Id
    @Column
    private String routeId;
    @Column
    private String path;
    @Column
    private String uri;

//    @Transient
//    public boolean isNew;

//    public RouteInfo setNew() {
//        this.isNew = true;
//        return this;
//    }

//    @Override
//    public String getId() {
//        return routeId;
//    }
//
//    @Override
//    public boolean isNew() {
//        return false;
//    }
}
