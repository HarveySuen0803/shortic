package com.harvey.common.exception;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
public class ExceptionType {
    private final String exceptionType;
    
    public ExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
    
    public static final ExceptionType SERVER = new ExceptionType("server");
    
    public static final ExceptionType CLIENT = new ExceptionType("client");
    
    public static final ExceptionType REMOTE = new ExceptionType("remote");
    
    public String value() {
        return exceptionType;
    }
}
