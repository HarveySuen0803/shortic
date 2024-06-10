package com.harvey.common.exception;

import com.harvey.common.constant.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public class ServerException extends BaseException {
    public ServerException(Exception e) {
        super(e);
    }
    
    public ServerException(int code, String message) {
        super(code, message);
    }
    
    public ServerException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    
    public <T> ServerException(Result<T> result) {
        super(result);
    }
    
    public <T> ServerException(Result<T> result, Throwable cause) {
        super(result, cause);
    }
}