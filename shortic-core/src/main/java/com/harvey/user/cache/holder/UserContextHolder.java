package com.harvey.user.cache.holder;

import com.harvey.user.domain.UserDo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-26
 */
public class UserContextHolder {
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    public static UserDo getUser() {
        Authentication authentication = UserContextHolder.getAuthentication();
        if (authentication == null) {
            return null;
        }
        
        return (UserDo) authentication.getPrincipal();
    }
    
    public static String getUsername() {
        UserDo userDo = UserContextHolder.getUser();
        if (userDo == null) {
            return null;
        }
        
        return userDo.getUsername();
    }
    
    public static Long getUserId() {
        UserDo userDo = UserContextHolder.getUser();
        if (userDo == null) {
            return null;
        }
        
        return userDo.getId();
    }
    
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        UserDo userDo = UserContextHolder.getUser();
        if (userDo == null) {
            return null;
        }
        
        return userDo.getAuthorities();
    }
}