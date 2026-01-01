package com.wwfinance.common.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResourceNotFoundException extends RuntimeException {


    //错误码
    private Integer code;
    //错误消息
    private String msg;


    public ResourceNotFoundException(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }
}