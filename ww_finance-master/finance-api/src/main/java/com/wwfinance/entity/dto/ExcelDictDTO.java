package com.wwfinance.entity.dto;

import lombok.Data;

@Data
public class ExcelDictDTO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer value;

    private String dictCode;
}