package com.wwfinance.controller.api;

import com.wwfinance.entity.dto.AiAgentRequest;
import com.wwfinance.entity.dto.AiAgentResponse;
import com.wwfinance.service.AiAgentService;
import com.wwfinance.utils.TokenUtil;
import com.wwfinance.common.result.PccAjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AI Agent控制器
 */
@RestController
@RequestMapping("/api/core/aiAgent")
@Slf4j
public class AiAgentController {
    
    @Autowired
    private AiAgentService aiAgentService;
    
    /**
     * 处理AI Agent请求
     * @param request AI Agent请求
     * @param authorizationHeader 认证头
     * @return AI Agent响应
     */
    @Operation(summary = "处理AI Agent请求")
    @PostMapping("/process")
    public PccAjaxResult processRequest(@RequestBody AiAgentRequest request, 
                                       @RequestHeader("Authorization") String authorizationHeader) {
        log.info("AI Agent请求: {}", request);
        
        // 从token中获取用户ID
        Long userId = getUserIdFromToken(authorizationHeader);
        request.setUserId(userId);
        
        AiAgentResponse response = aiAgentService.processRequest(request);
        return new PccAjaxResult(200, "处理成功", response);
    }
    
    /**
     * 获取金融建议
     * @param query 查询内容
     * @param authorizationHeader 认证头
     * @return 金融建议
     */
    @Operation(summary = "获取金融建议")
    @GetMapping("/financialAdvice")
    public PccAjaxResult getFinancialAdvice(@RequestParam String query, 
                                           @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);
        String advice = aiAgentService.getFinancialAdvice(userId, query);
        return new PccAjaxResult(200, "获取成功", advice);
    }
    
    /**
     * 评估借款风险
     * @param borrowAmount 借款金额
     * @param borrowTerm 借款期限
     * @param authorizationHeader 认证头
     * @return 风险评估结果
     */
    @Operation(summary = "评估借款风险")
    @GetMapping("/borrowRisk")
    public PccAjaxResult evaluateBorrowRisk(@RequestParam Double borrowAmount, 
                                           @RequestParam Integer borrowTerm, 
                                           @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);
        String riskEvaluation = aiAgentService.evaluateBorrowRisk(userId, borrowAmount, borrowTerm);
        return new PccAjaxResult(200, "评估成功", riskEvaluation);
    }
    
    /**
     * 提供投资建议
     * @param riskLevel 风险等级
     * @param investmentAmount 投资金额
     * @param authorizationHeader 认证头
     * @return 投资建议
     */
    @Operation(summary = "提供投资建议")
    @GetMapping("/investmentAdvice")
    public PccAjaxResult getInvestmentAdvice(@RequestParam String riskLevel, 
                                           @RequestParam Double investmentAmount, 
                                           @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);
        String advice = aiAgentService.getInvestmentAdvice(userId, riskLevel, investmentAmount);
        return new PccAjaxResult(200, "获取成功", advice);
    }
    
    /**
     * 回答用户问题
     * @param question 用户问题
     * @param authorizationHeader 认证头
     * @return 回答内容
     */
    @Operation(summary = "回答用户问题")
    @GetMapping("/answerQuestion")
    public PccAjaxResult answerUserQuestion(@RequestParam String question, 
                                           @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);
        String answer = aiAgentService.answerUserQuestion(userId, question);
        return new PccAjaxResult(200, "回答成功", answer);
    }
    
    /**
     * 从token中获取用户ID
     * @param authorizationHeader 认证头
     * @return 用户ID
     */
    private Long getUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader;
        log.info("token:{}", token);
        Map<String, String> tokenInfo = TokenUtil.getUserInfoFromToken(token);
        return Long.parseLong(tokenInfo.get("token_user_id"));
    }
}
