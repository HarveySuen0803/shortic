package com.harvey.user.common;

import cn.hutool.core.date.DateField;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
public class UserConstant {
    public static final byte[] LOGIN_TOKEN_KEY = "login_token_key".getBytes();
    
    public static final String USER_DETAILS_KEY = "userDetails";
    
    public static final int LOGIN_TOKEN_TIMEOUT = 1;
    
    public static final DateField LOGIN_TOKEN_TIMEOUT_UNIT = DateField.MONTH;
}