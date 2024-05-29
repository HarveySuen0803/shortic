package com.harvey.common.exception;

import com.harvey.common.constant.Result;
import jakarta.servlet.http.HttpServletRequest;
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
    public Result handleException(Exception e, HttpServletRequest req) {
        log.error("Catch exception, Request Url: {}, Request Method: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getMessage());
        return Result.FAILURE;
    }
    
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest req) {
        log.error("Catch runtime exception, Request Url: {}, Request Method: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getMessage());
        return Result.FAILURE;
    }
    
    @ExceptionHandler(BaseException.class)
    public Result handleBaseException(BaseException e, HttpServletRequest req) {
        log.error("Catch base exception, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(ClientException.class)
    public Result handleClientException(ClientException e, HttpServletRequest req) {
        log.error("Catch client exception, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
    
    
    @ExceptionHandler(ServerException.class)
    public Result handleServerException(ServerException e, HttpServletRequest req) {
        log.error("Catch server exception, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(RemoteException.class)
    public Result handleRemoteException(RemoteException e, HttpServletRequest req) {
        log.error("Catch remote exception, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
}