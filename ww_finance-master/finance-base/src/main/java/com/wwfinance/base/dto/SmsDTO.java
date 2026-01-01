package com.wwfinance.base.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "短信")
public class SmsDTO {

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "消息内容")
    private String message;
}