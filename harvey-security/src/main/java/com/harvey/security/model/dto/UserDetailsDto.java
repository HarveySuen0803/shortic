package com.harvey.security.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-03
 */
@Data
public class UserDetailsDto implements UserDetails {
    private Long id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private Byte accountExpiredFlag;
    
    private Byte credentialsExpiredFlag;
    
    private Byte accountLockedFlag;
    
    private Byte deletedFlag;
    
    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities;
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Override
    public boolean isAccountNonExpired() {
        return accountExpiredFlag == 0;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountLockedFlag == 0;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpiredFlag == 0;
    }
    
    @Override
    public boolean isEnabled() {
        return deletedFlag == 0;
    }
}
