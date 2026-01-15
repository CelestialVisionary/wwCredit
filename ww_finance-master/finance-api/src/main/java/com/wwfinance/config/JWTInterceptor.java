package com.wwfinance.config;

import com.wwfinance.common.result.PccAjaxResult;
import com.wwfinance.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JWT令牌验证拦截器
 */
@Slf4j
@Component
public class JWTInterceptor implements HandlerInterceptor {

    /**
     * 预处理，在请求处理之前进行调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的Token
        var token = request.getHeader("Authorization");
        
        // 2. 检查Token是否存在
        if (token == null || token.isEmpty()) {
            return writeErrorResponse(response, "Token不存在");
        }
        
        // 3. 检查Token格式是否正确
        if (!token.startsWith(TokenUtil.getTokenPrefix())) {
            return writeErrorResponse(response, "Token格式错误");
        }
        
        // 4. 验证Token是否过期
        if (TokenUtil.isTokenExpired(token)) {
            return writeErrorResponse(response, "Token已过期");
        }
        
        // 5. Token验证通过，放行请求
        return true;
    }
    
    /**
     * 写入错误响应
     */
    private boolean writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        var result = new PccAjaxResult(401, message);
        var objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
        return false;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理之后进行调用，但是在视图被渲染之前
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求结束之后被调用，也就是在DispatcherServlet渲染了对应的视图之后执行
    }
}