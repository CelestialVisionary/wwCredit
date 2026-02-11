package com.wwfinance.entity.dto;

import lombok.Data;

/**
 * AI Agent响应DTO
 */
@Data
public class AiAgentResponse {
    
    /**
     * 响应状态
     */
    private String status;
    
    /**
     * 响应内容
     */
    private String content;
    
    /**
     * 响应类型
     */
    private String type;
    
    /**
     * 处理时间
     */
    private long processingTime;
}
