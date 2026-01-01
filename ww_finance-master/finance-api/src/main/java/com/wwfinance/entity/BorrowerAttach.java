package com.wwfinance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 借款人上传资源表
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("borrower_attach")
public class BorrowerAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 借款人id
     */
    @TableField("borrower_id")
    private Long borrowerId;

    /**
     * 图片类型（idCard1：身份证正面，idCard2：身份证反面，house：房产证，car：车）
     */
    @TableField("image_type")
    private String imageType;

    /**
     * 图片路径
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 图片名称
     */
    @TableField("image_name")
    private String imageName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除(1:已删除，0:未删除)
     */
    @TableField("is_deleted")
    private Boolean deleted;


}
