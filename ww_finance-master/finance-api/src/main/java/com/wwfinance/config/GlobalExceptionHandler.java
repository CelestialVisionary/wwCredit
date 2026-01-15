package com.wwfinance.config;

import com.wwfinance.common.result.PccAjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * SQL语法异常处理
     * @param e BadSqlGrammarException
     * @return 统一响应结果
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public PccAjaxResult handleBadSqlGrammarException(BadSqlGrammarException e) {
        log.error("SQL语法错误：{}", e.getMessage(), e);
        return new PccAjaxResult(500, "数据库操作失败，请检查SQL语法");
    }

    /**
     * 其他异常处理
     * @param e Exception
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    public PccAjaxResult handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return new PccAjaxResult(500, "系统异常，请稍后重试");
    }
}