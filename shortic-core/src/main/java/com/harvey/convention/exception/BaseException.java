package com.harvey.convention.exception;

import com.harvey.convention.result.ResultStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    private final int code;
    
    private final String message;

    public BaseException(int code, String message) {
        super(message, null);
        this.code = code;
        this.message = message;
    }
    
    public BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
    
    public BaseException(ResultStatus resultStatus) {
        this(resultStatus.getCode(), resultStatus.getMessage());
    }
    
    public BaseException(ResultStatus resultStatus, Throwable cause) {
        this(resultStatus.getCode(), resultStatus.getMessage(), cause);
    }
}
