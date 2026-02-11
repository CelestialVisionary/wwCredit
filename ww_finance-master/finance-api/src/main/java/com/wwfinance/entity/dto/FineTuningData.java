package com.wwfinance.entity.dto;

import lombok.Data;

/**
 * 微调数据DTO
 */
@Data
public class FineTuningData {
    
    /**
     * 数据ID
     */
    private Long id;
    
    /**
     * 场景类型
     * financialAdvice: 金融建议
     * borrowRisk: 借款风险评估
     * investmentAdvice: 投资建议
     * question: 智能问答
     */
    private String scenario;
    
    /**
     * 输入提示词
     */
    private String prompt;
    
    /**
     * 期望输出
     */
    private String completion;
    
    /**
     * 创建时间
     */
    private String createdAt;
}