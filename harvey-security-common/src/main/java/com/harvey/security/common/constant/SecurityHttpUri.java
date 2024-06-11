package com.harvey.security.common.constant;

import com.harvey.common.constant.HttpUri;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
public class SecurityHttpUri extends HttpUri {
    public static final String LOGIN = "/api/security/v1/login";
    
    public static final String LOGOUT = "/api/security/v1/logout";
    
    public static final String REFRESH = "/api/security/v1/login/refresh";
    
    public static final String REGISTER = "/api/security/v1/register";
}