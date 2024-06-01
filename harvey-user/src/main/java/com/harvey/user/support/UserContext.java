package com.harvey.user.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private Long userId;
    
    private String username;
    
    private Collection<? extends GrantedAuthority> authorities;
}
