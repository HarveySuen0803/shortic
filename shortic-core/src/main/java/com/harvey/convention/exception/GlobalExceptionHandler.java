package com.harvey.convention.exception;

import cn.hutool.http.server.HttpServerRequest;
import com.harvey.convention.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(HttpServerRequest req, Exception e) {
        log.error("Catch exception, Request Uri: {}, Request Method: {}, Exception Message: {}", req.getURI(), req.getMethod(), e.getMessage());
        return Result.failure();
    }
    
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(HttpServerRequest req, RuntimeException e) {
        log.error("Catch runtime exception, Request Uri: {}, Request Method: {}, Exception Message: {}", req.getURI(), req.getMethod(), e.getMessage());
        return Result.failure();
    }
    
    @ExceptionHandler(BaseException.class)
    public Result handleBaseException(HttpServerRequest req, BaseException e) {
        log.error("Catch base exception, Request Uri: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getURI(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
}
