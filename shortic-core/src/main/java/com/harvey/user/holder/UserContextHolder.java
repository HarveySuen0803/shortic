package com.harvey.user.holder;

import com.harvey.common.exception.ServerException;
import com.harvey.user.entity.domain.UserDo;
import com.harvey.user.result.UserResult;
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
            throw new ServerException(UserResult.USER_NOT_FOUND);
        }
        
        return (UserDo) authentication.getPrincipal();
    }
    
    public static String getUsername() {
        return getUser().getUsername();
    }
    
    public static Long getUserId() {
        return getUser().getId();
    }
    
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return getUser().getAuthorities();
    }
}