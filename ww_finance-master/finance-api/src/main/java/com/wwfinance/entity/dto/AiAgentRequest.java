package com.wwfinance.entity.dto;

import lombok.Data;

/**
 * AI Agent请求DTO
 */
@Data
public class AiAgentRequest {
    
    /**
     * 请求类型
     */
    private String type;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 查询内容
     */
    private String query;
    
    /**
     * 附加参数
     */
    private String params;
}
