package com.harvey.user.support;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-26
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> USER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();
    
    public static void setUserContext(UserContext userContext) {
        USER_CONTEXT_THREAD_LOCAL.set(userContext);
    }
    
    public static UserContext getUserContext() {
        return USER_CONTEXT_THREAD_LOCAL.get();
    }
    
    public static String getUsername() {
        return getUserContext().getUsername();
    }
    
    public static Long getUserId() {
        return getUserContext().getUserId();
    }
    
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return getUserContext().getAuthorities();
    }
    
    public static void clear() {
        USER_CONTEXT_THREAD_LOCAL.remove();
    }
}