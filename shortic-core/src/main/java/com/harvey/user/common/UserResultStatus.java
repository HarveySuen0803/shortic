package com.harvey.user.common;

import com.harvey.convention.result.ResultStatus;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public enum UserResultStatus implements ResultStatus {
    USER_NOT_FOUND(1001, "User not found");
    
    private final int code;
    
    private final String message;
    
    UserResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public int getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
