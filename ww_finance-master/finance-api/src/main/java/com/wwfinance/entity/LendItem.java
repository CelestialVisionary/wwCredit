package com.wwfinance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 标的出借记录表
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("lend_item")
public class LendItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 投资编号
     */
    @TableField("lend_item_no")
    private String lendItemNo;

    /**
     * 标的id
     */
    @TableField("lend_id")
    private Long lendId;

    /**
     * 投资用户id
     */
    @TableField("invest_user_id")
    private Long investUserId;

    /**
     * 投资人名称
     */
    @TableField("invest_name")
    private String investName;

    /**
     * 投资金额
     */
    @TableField("invest_amount")
    private BigDecimal investAmount;

    /**
     * 年化利率
     */
    @TableField("lend_year_rate")
    private BigDecimal lendYearRate;

    /**
     * 投资时间
     */
    @TableField("invest_time")
    private LocalDateTime investTime;

    /**
     * 开始日期
     */
    @TableField("lend_start_date")
    private LocalDate lendStartDate;

    /**
     * 结束日期
     */
    @TableField("lend_end_date")
    private LocalDate lendEndDate;

    /**
     * 预期收益
     */
    @TableField("expect_amount")
    private BigDecimal expectAmount;

    /**
     * 实际收益
     */
    @TableField("real_amount")
    private BigDecimal realAmount;

    /**
     * 状态（0：默认 1：已支付 2：已还款）
     */
    @TableField("status")
    private Integer status;

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
