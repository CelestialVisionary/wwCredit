package com.wwfinance.service;

import com.wwfinance.entity.dto.AiAgentRequest;
import com.wwfinance.entity.dto.AiAgentResponse;

/**
 * AI Agent服务接口
 */
public interface AiAgentService {
    
    /**
     * 处理AI Agent请求
     * @param request AI Agent请求
     * @return AI Agent响应
     */
    AiAgentResponse processRequest(AiAgentRequest request);
    
    /**
     * 获取金融建议
     * @param userId 用户ID
     * @param query 查询内容
     * @return 金融建议
     */
    String getFinancialAdvice(Long userId, String query);
    
    /**
     * 评估借款风险
     * @param userId 用户ID
     * @param borrowAmount 借款金额
     * @param borrowTerm 借款期限
     * @return 风险评估结果
     */
    String evaluateBorrowRisk(Long userId, Double borrowAmount, Integer borrowTerm);
    
    /**
     * 提供投资建议
     * @param userId 用户ID
     * @param riskLevel 风险等级
     * @param investmentAmount 投资金额
     * @return 投资建议
     */
    String getInvestmentAdvice(Long userId, String riskLevel, Double investmentAmount);
    
    /**
     * 回答用户问题
     * @param userId 用户ID
     * @param question 用户问题
     * @return 回答内容
     */
    String answerUserQuestion(Long userId, String question);
}
