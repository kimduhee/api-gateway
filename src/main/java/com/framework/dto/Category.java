package com.framework.dto;

import lombok.Data;

import java.util.List;

@Data
public class Category {
    private String cateId;
    private String cateNm;
    private List<Goods> goodsList;
}
