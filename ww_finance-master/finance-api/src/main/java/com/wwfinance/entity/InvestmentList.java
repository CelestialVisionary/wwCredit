package com.wwfinance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 投资列表表
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("investment_list")
public class InvestmentList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 借款用户id
     */
    private String mobile;

    /**
     * 借款标题
     */
    private String loanTitle;

    /**
     * 借款金额
     */
    private Long loanMoney;

    /**
     * 年利率
     */
    private String borrowYearRate;

    /**
     * 借款期限
     */
    private String loanTerm;

    /**
     * '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
     */
    private String repaymentType;

    /**
     * 借款进度
     */
    private String loanProgress;

    /**
     * 操作
     */
    private String operate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除(1:已删除，0:未删除)
     */
    private Boolean isDeleted;


}
