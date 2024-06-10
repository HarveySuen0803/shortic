package com.harvey.common.support;

import com.harvey.common.constant.Result;
import com.harvey.common.exception.BaseException;
import com.harvey.common.exception.ClientException;
import com.harvey.common.exception.RemoteException;
import com.harvey.common.exception.ServerException;
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
        log.error("Exception, Request Url: {}, Request Method: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getMessage());
        return Result.FAILURE;
    }
    
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest req) {
        log.error("RuntimeException, Request Url: {}, Request Method: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getMessage());
        return Result.FAILURE;
    }
    
    @ExceptionHandler(BaseException.class)
    public Result handleBaseException(BaseException e, HttpServletRequest req) {
        log.error("BaseException, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(ClientException.class)
    public Result handleClientException(ClientException e, HttpServletRequest req) {
        log.error("ClientException, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(ServerException.class)
    public Result handleServerException(ServerException e, HttpServletRequest req) {
        log.error("ServerException, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(RemoteException.class)
    public Result handleRemoteException(RemoteException e, HttpServletRequest req) {
        log.error("RemoteException, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Code: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
}