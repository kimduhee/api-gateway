package com.framework.gw.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name="INTERFACE_INFO")
public class InterfaceInfoEntity{

    @Id
    @Column
    private String interfaceId;

    @Column
    private String packageName;

    @Column
    private String orgName;

    @Column
    private String newName;
}
