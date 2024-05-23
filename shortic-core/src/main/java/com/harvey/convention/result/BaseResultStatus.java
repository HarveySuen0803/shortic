package com.harvey.convention.result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public enum BaseResultStatus {
    SUCCESS(200, "success"),
    FAILURE(400, "failure");
    
    public final int code;
    
    public final String message;
    
    BaseResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}