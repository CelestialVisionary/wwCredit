package com.wwfinance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分等级表
 * </p>
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("integral_grade")
public class IntegralGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 积分区间开始
     */
    @TableField("integral_start")
    private Integer integralStart;

    /**
     * 积分区间结束
     */
    @TableField("integral_end")
    private Integer integralEnd;

    /**
     * 借款额度
     */
    @TableField("borrow_amount")
    private BigDecimal borrowAmount;

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
    @TableLogic
    private Boolean deleted;


}
