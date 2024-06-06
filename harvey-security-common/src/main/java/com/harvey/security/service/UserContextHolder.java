package com.harvey.security.service;

import com.harvey.security.entity.UserContext;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-06
 */
public interface UserContextHolder {
    void setUserContext(UserContext userContext);
    
    UserContext getUserContext();
    
    String getUsername();
    
    Long getUserId();
    
    void clear();
}
