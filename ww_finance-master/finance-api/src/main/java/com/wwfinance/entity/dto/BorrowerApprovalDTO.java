package com.wwfinance.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "借款人审批")
public class BorrowerApprovalDTO {
    @Schema(description = "id")
    private Long borrowerId;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "身份证信息是否正确")
    private Boolean isIdCardOk;
    @Schema(description = "房产信息是否正确")
    private Boolean isHouseOk;
    @Schema(description = "车辆信息是否正确")
    private Boolean isCarOk;
    @Schema(description = "基本信息积分")
    private Integer infoIntegral;
}
