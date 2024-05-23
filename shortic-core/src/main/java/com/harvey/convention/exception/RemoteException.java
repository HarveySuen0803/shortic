package com.harvey.convention.exception;

import com.harvey.convention.result.ResultStatus;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public class RemoteException extends BaseException {
    public RemoteException(int code, String message) {
        super(code, message);
    }
    
    public RemoteException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    
    public RemoteException(ResultStatus resultStatus) {
        super(resultStatus);
    }
    
    public RemoteException(ResultStatus resultStatus, Throwable cause) {
        super(resultStatus, cause);
    }
}
