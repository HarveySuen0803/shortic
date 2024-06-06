package com.harvey.security.service.impl;

import com.harvey.security.entity.UserContext;
import com.harvey.security.service.UserContextHolder;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-26
 */
public class UserContextHolderImpl implements UserContextHolder {
    private static final ThreadLocal<UserContext> USER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();
    
    public void setUserContext(UserContext userContext) {
        USER_CONTEXT_THREAD_LOCAL.set(userContext);
    }
    
    public UserContext getUserContext() {
        return USER_CONTEXT_THREAD_LOCAL.get();
    }
    
    public String getUsername() {
        return getUserContext().getUsername();
    }
    
    public Long getUserId() {
        return getUserContext().getUserId();
    }
    
    public void clear() {
        USER_CONTEXT_THREAD_LOCAL.remove();
    }
}