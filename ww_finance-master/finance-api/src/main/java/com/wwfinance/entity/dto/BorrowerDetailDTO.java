package com.wwfinance.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description="借款人信息详情")
public class BorrowerDetailDTO {
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "身份证号")
    private String idCard;
    @Schema(description = "手机")
    private String mobile;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "年龄")
    private Integer age;
    @Schema(description = "学历")
    private String education;
    @Schema(description = "是否结婚")
    private String marry;
    @Schema(description = "行业")
    private String industry;
    @Schema(description = "月收入")
    private String income;
    @Schema(description = "还款来源")
    private String returnSource;
    @Schema(description = "联系人名称")
    private String contactsName;
    @Schema(description = "联系人手机")
    private String contactsMobile;
    @Schema(description = "联系人关系")
    private String contactsRelation;
    @Schema(description = "审核状态")
    private String status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "借款人附件资料")
    private List<BorrowerAttachDTO> borrowerAttachVOList;
}
