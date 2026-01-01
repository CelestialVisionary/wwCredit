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
 * 标的出借回款记录表
 * </p>
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("lend_item_return")
public class LendItemReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标的还款id
     */
    @TableField("lend_return_id")
    private Long lendReturnId;

    /**
     * 标的项id
     */
    @TableField("lend_item_id")
    private Long lendItemId;

    /**
     * 标的id
     */
    @TableField("lend_id")
    private Long lendId;

    /**
     * 出借用户id
     */
    @TableField("invest_user_id")
    private Long investUserId;

    /**
     * 出借金额
     */
    @TableField("invest_amount")
    private BigDecimal investAmount;

    /**
     * 当前的期数
     */
    @TableField("current_period")
    private Integer currentPeriod;

    /**
     * 年化利率
     */
    @TableField("lend_year_rate")
    private BigDecimal lendYearRate;

    /**
     * 还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本
     */
    @TableField("return_method")
    private Integer returnMethod;

    /**
     * 本金
     */
    @TableField("principal")
    private BigDecimal principal;

    /**
     * 利息
     */
    @TableField("interest")
    private BigDecimal interest;

    /**
     * 本息
     */
    @TableField("total")
    private BigDecimal total;

    /**
     * 手续费
     */
    @TableField("fee")
    private BigDecimal fee;

    /**
     * 还款时指定的还款日期
     */
    @TableField("return_date")
    private LocalDate returnDate;

    /**
     * 实际发生的还款时间
     */
    @TableField("real_return_time")
    private LocalDateTime realReturnTime;

    /**
     * 是否逾期
     */
    @TableField("is_overdue")
    private Boolean overdue;

    /**
     * 逾期金额
     */
    @TableField("overdue_total")
    private BigDecimal overdueTotal;

    /**
     * 状态（0-未归还 1-已归还）
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
