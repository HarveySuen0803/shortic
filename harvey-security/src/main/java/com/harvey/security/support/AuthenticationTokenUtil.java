package com.harvey.security.support;

import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.harvey.user.common.constant.UserConstant;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-01
 */
public class AuthenticationTokenUtil {
    /**
     * Generate access_token for authorization and authentication.
     */
    public static String genAccessToken(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        String authoritiesJson = JSON.toJSONString(authorities);
        
        HashMap<String, Object> accessTokenPayload = new HashMap<>();
        accessTokenPayload.put(JWTPayload.ISSUED_AT, DateTime.now());
        accessTokenPayload.put(JWTPayload.EXPIRES_AT, DateTime.now().offset(UserConstant.LOGIN_TOKEN_TIMEOUT_UNIT, UserConstant.LOGIN_TOKEN_TIMEOUT));
        accessTokenPayload.put(JWTPayload.NOT_BEFORE, DateTime.now());
        accessTokenPayload.put(UserConstant.USER_ID, userId);
        accessTokenPayload.put(UserConstant.USERNAME, username);
        accessTokenPayload.put(UserConstant.PASSWORD, password);
        accessTokenPayload.put(UserConstant.AUTHORITIES, authoritiesJson);
        
        String accessToken = JWTUtil.createToken(accessTokenPayload, UserConstant.ACCESS_TOKEN_KEY);
        
        return accessToken;
    }
    
    /**
     * Generate refresh_token to create a new access_token when the access_token expires.
     */
    public static String genRefreshToken(Long userId, String username) {
        HashMap<String, Object> refreshTokenPayload = new HashMap<>();
        refreshTokenPayload.put(UserConstant.USER_ID, userId);
        refreshTokenPayload.put(UserConstant.USERNAME, username);
        
        String refreshToken = JWTUtil.createToken(refreshTokenPayload, UserConstant.REFRESH_TOKEN_KEY);
        
        return refreshToken;
    }
}
