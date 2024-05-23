package com.harvey.convention.exception;

import com.harvey.convention.result.ResultStatus;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public class ClientException extends BaseException {
    public ClientException(int code, String message) {
        super(code, message);
    }
    
    public ClientException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    
    public ClientException(ResultStatus resultStatus) {
        super(resultStatus);
    }
    
    public ClientException(ResultStatus resultStatus, Throwable cause) {
        super(resultStatus, cause);
    }
}
