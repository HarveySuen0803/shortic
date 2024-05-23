package com.harvey.convention.result;

import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Data
public class Result<T> {
    private int code;
    
    private String message;
    
    private T data;
    
    public Result() {
    }
    
    public Result(int code) {
        this(code, null, null);
    }
    
    public Result(int code, String message) {
        this(code, message, null);
    }
    
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Result<T> success() {
        return success(BaseResultStatus.SUCCESS.code, BaseResultStatus.SUCCESS.message, null);
    }
    
    public static <T> Result<T> success(T data) {
        return success(BaseResultStatus.SUCCESS.code, BaseResultStatus.SUCCESS.message, data);
    }
    
    public static <T> Result<T> success(String message) {
        return success(BaseResultStatus.SUCCESS.code, message, null);
    }
    
    public static <T> Result<T> success(int code, String message) {
        return success(code, message, null);
    }
    
    public static <T> Result<T> success(int code, T data) {
        return success(code, BaseResultStatus.SUCCESS.message, data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return success(BaseResultStatus.SUCCESS.code, message, data);
    }
    
    public static <T> Result<T> success(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
    
    public static <T> Result<T> failure() {
        return failure(BaseResultStatus.FAILURE.code, BaseResultStatus.FAILURE.message, null);
    }
    
    public static <T> Result<T> failure(T data) {
        return failure(BaseResultStatus.FAILURE.code, BaseResultStatus.FAILURE.message, data);
    }
    
    public static <T> Result<T> failure(String message) {
        return failure(BaseResultStatus.FAILURE.code, message, null);
    }
    
    public static <T> Result<T> failure(String message, T data) {
        return failure(BaseResultStatus.FAILURE.code, message, data);
    }
    
    public static <T> Result<T> failure(int code, String message) {
        return failure(code, message, null);
    }
    
    public static <T> Result<T> failure(int code, T data) {
        return failure(code, BaseResultStatus.FAILURE.message, data);
    }
    
    public static <T> Result<T> failure(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
