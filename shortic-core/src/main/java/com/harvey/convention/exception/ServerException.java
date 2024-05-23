package com.harvey.convention.exception;

import com.harvey.convention.result.ResultStatus;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public class ServerException extends BaseException {
    public ServerException(int code, String message) {
        super(code, message);
    }
    
    public ServerException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    
    public ServerException(ResultStatus resultStatus) {
        super(resultStatus);
    }
    
    public ServerException(ResultStatus resultStatus, Throwable cause) {
        super(resultStatus, cause);
    }
}