package com.wwfinance.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "借款人附件资料")
public class BorrowerAttachDTO {

    @Schema(description = "图片类型（idCard1：身份证正面，idCard2：身份证反面，house：房产证，car：车）")
    private String imageType;
    @Schema(description = "图片路径")
    private String imageUrl;
}

