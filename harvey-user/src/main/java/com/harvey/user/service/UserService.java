package com.harvey.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.user.common.entity.po.UserPo;
import com.harvey.user.common.entity.vo.UserVo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public interface UserService extends IService<UserPo> {
    /**
     * Build the access_token and cache it.
     */
    String buildAccessTokenCache(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities);
    
    /**
     * Build the refresh_token and cache it.
     */
    String buildRefreshTokenCache(Long userId, String username);
    
    UserVo getUserVo(String username);
    
    boolean isUserExists(String username, String email);
    
    boolean isUsernameExists(String username);
    
    boolean isEmailExists(String email);
}