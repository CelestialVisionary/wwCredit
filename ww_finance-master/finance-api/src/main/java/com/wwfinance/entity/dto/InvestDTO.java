package com.wwfinance.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "投标信息")
public class InvestDTO {
    private Long lendId;
    @Schema(description = "投标金额")
    private String investAmount;
    @Schema(description = "用户id")
    private Long investUserId;
    @Schema(description = "用户姓名")
    private String investName;
}
