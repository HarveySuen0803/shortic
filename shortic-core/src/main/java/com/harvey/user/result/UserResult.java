package com.harvey.user.result;

import com.harvey.common.result.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
public class UserResult extends Result {
    public static final Result LOGIN_FAILURE = new Result(1001, "login failure");
    
    public static final Result LOGIN_TOKEN_EXPIRED = new Result(1002, "login token expired");
    
    public static final Result LOGOUT_FAILURE = new Result(1101, "logout failure");
    
    public static final Result USER_NOT_FOUND = new Result(1201, "user not found");
}