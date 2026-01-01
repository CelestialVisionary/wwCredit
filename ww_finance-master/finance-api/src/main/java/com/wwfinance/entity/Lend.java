package com.wwfinance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 标的准备表
 * </p>
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("lend")
public class Lend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 借款用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 借款信息id
     */
    @TableField("borrow_info_id")
    private Long borrowInfoId;

    /**
     * 标的编号
     */
    @TableField("lend_no")
    private String lendNo;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 标的金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 投资期数
     */
    @TableField("period")
    private Integer period;

    /**
     * 年化利率
     */
    @TableField("lend_year_rate")
    private BigDecimal lendYearRate;

    /**
     * 平台服务费率
     */
    @TableField("service_rate")
    private BigDecimal serviceRate;

    /**
     * 还款方式
     */
    @TableField("return_method")
    private Integer returnMethod;

    /**
     * 最低投资金额
     */
    @TableField("lowest_amount")
    private BigDecimal lowestAmount;

    /**
     * 已投金额
     */
    @TableField("invest_amount")
    private BigDecimal investAmount;

    /**
     * 投资人数
     */
    @TableField("invest_num")
    private Integer investNum;

    /**
     * 发布日期
     */
    @TableField("publish_date")
    private LocalDateTime publishDate;

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
     * 说明
     */
    @TableField("lend_info")
    private String lendInfo;

    /**
     * 平台预期收益
     */
    @TableField("expect_amount")
    private BigDecimal expectAmount;

    /**
     * 实际收益
     */
    @TableField("real_amount")
    private BigDecimal realAmount;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 审核时间
     */
    @TableField("check_time")
    private LocalDateTime checkTime;

    /**
     * 审核用户id
     */
    @TableField("check_admin_id")
    private Long checkAdminId;

    /**
     * 放款时间
     */
    @TableField("payment_time")
    private LocalDateTime paymentTime;

    /**
     * 放款人id
     */
    @TableField("payment_admin_id")
    private LocalDateTime paymentAdminId;

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

    @Schema(description = "其他参数")
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();
}
