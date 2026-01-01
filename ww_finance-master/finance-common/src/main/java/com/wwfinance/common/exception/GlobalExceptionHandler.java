package com.wwfinance.common.exception;

import com.wwfinance.common.result.PccAjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public PccAjaxResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new PccAjaxResult(500, e.getMessage());
    }

    @ExceptionHandler(value = BadSqlGrammarException.class)
    public PccAjaxResult handleException(BadSqlGrammarException e){
        log.error(e.getMessage(), e);
        return new PccAjaxResult(101, "sql语法错误");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public PccAjaxResult handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new PccAjaxResult(e.getCode(), e.getMsg());
    }
}
