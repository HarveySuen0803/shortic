package com.harvey.common.result;

import lombok.Getter;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Getter
public class Result<T> {
    private int code;
    
    private String message;
    
    private T data;
    
    public Result() {
    }
    
    public Result(int code, String message) {
        this(code, message, null);
    }
    
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static Result success() {
        return success(SUCCESS.code, SUCCESS.message, null);
    }
    
    public static Result success(String message) {
        return success(SUCCESS.code, message, null);
    }
    
    public static <T> Result<T> success(T data) {
        return success(SUCCESS.code, SUCCESS.message, data);
    }
    
    public static <T> Result<T> success(int code, String message) {
        return success(code, message, null);
    }
    
    public static <T> Result<T> success(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
    
    public static Result failure(String message) {
        return success(FAILURE.code, message, null);
    }
    
    public static Result failure() {
        return success(FAILURE.code, FAILURE.message, null);
    }
    
    public static <T> Result<T> failure(T data) {
        return failure(FAILURE.code, FAILURE.message, data);
    }
    
    public static <T> Result<T> failure(int code, String message) {
        return failure(code, message, null);
    }
    
    public static <T> Result<T> failure(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
    
    // forbidden
    public static Result forbidden() {
        return success(FORBIDDEN.code, FORBIDDEN.message, null);
    }
    
    public static Result forbidden(String message) {
        return success(FORBIDDEN.code, message, null);
    }
    
    public static <T> Result<T> forbidden(T data) {
        return failure(FORBIDDEN.code, FORBIDDEN.message, data);
    }
    
    public static <T> Result<T> forbidden(String message, T data) {
        return failure(FORBIDDEN.code, message, data);
    }
    
    // unauthorized
    public static Result unauthorized() {
        return success(UNAUTHORIZED.code, UNAUTHORIZED.message, null);
    }
    
    public static Result unauthorized(String message) {
        return success(UNAUTHORIZED.code, message, null);
    }
    
    public static <T> Result<T> unauthorized(T data) {
        return failure(UNAUTHORIZED.code, UNAUTHORIZED.message, data);
    }
    
    
    public static final Result SUCCESS = new Result(200, "success");
    
    public static final Result FAILURE = new Result(400, "failure");
    
    public static final Result UNAUTHORIZED = new Result(401, "unauthorized");
    
    public static final Result FORBIDDEN = new Result(403, "forbidden");
    
    public static final Result NOT_FOUND = new Result(404, "not found");
    
    public static final Result INTERNAL_SERVER_ERROR = new Result(500, "internal server error");
    
    public static final Result BAD_GATEWAY = new Result(502, "bad gateway");
    
    public static final Result SERVICE_UNAVAILABLE = new Result(503, "service unavailable");
    
    public static final Result GATEWAY_TIMEOUT = new Result(504, "gateway timeout");
    
    public static final Result USER_NOT_FOUND = new Result(601, "user not found");
    
    public static final Result PARAM_INVALID = new Result(602, "param invalid");
}