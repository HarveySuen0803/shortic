package com.harvey.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.constant.Constant;
import com.harvey.common.exception.ClientException;
import com.harvey.security.support.AuthenticationTokenUtil;
import com.harvey.security.constant.SecurityCacheKey;
import com.harvey.user.common.constant.UserResult;
import com.harvey.user.common.model.po.UserPo;
import com.harvey.user.common.model.vo.UserVo;
import com.harvey.user.mapper.UserMapper;
import com.harvey.user.service.*;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {
    @Resource
    private RedisTemplate redisTemplate;
    
    @Override
    public String buildAccessTokenCache(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        String accessToken = AuthenticationTokenUtil.genAccessToken(userId, username, password, authorities);
        
        redisTemplate.opsForValue().set(
            SecurityCacheKey.ACCESS_TOKEN.getKey(userId),
            accessToken,
            SecurityCacheKey.ACCESS_TOKEN.timeout,
            SecurityCacheKey.ACCESS_TOKEN.unit
        );
        
        return accessToken;
    }
    
    @Override
    public String buildRefreshTokenCache(Long userId, String username) {
        String refreshToken = AuthenticationTokenUtil.genRefreshToken(userId, username);
        
        redisTemplate.opsForValue().set(
            SecurityCacheKey.REFRESH_TOKEN.getKey(userId),
            refreshToken
        );
        
        return refreshToken;
    }
    
    @Override
    public UserVo getUserVo(String username) {
        UserPo userPo = lambdaQuery()
            .select(UserPo::getUsername, UserPo::getEmail)
            .eq(UserPo::getUsername, username)
            .eq(UserPo::getDeletedFlag, Constant.NOT_DELETED)
            .one();
        if (ObjUtil.isNull(userPo)) {
            throw new ClientException(UserResult.USER_NOT_FOUND);
        }
        
        UserVo userVo = BeanUtil.copyProperties(userPo, UserVo.class);
        
        return userVo;
    }
    
    @Override
    public boolean isUserExists(String username, String email) {
        boolean isExists = isUsernameExists(username) || isEmailExists(email);
        
        return isExists;
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        boolean isExists = lambdaQuery()
            .eq(UserPo::getUsername, username)
            .exists();
        
        return isExists;
    }
    
    @Override
    public boolean isEmailExists(String email) {
        boolean isExists = lambdaQuery()
            .eq(UserPo::getEmail, email)
            .exists();
        
        return isExists;
    }
}