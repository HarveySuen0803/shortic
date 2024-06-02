package com.harvey.security.support;

import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.harvey.security.constant.SecurityConstant;
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
        accessTokenPayload.put(JWTPayload.EXPIRES_AT, DateTime.now().offset(SecurityConstant.LOGIN_TOKEN_TIMEOUT_UNIT, SecurityConstant.LOGIN_TOKEN_TIMEOUT));
        accessTokenPayload.put(JWTPayload.NOT_BEFORE, DateTime.now());
        accessTokenPayload.put(SecurityConstant.USER_ID, userId);
        accessTokenPayload.put(SecurityConstant.USERNAME, username);
        accessTokenPayload.put(SecurityConstant.PASSWORD, password);
        accessTokenPayload.put(SecurityConstant.AUTHORITIES, authoritiesJson);
        
        String accessToken = JWTUtil.createToken(accessTokenPayload, SecurityConstant.ACCESS_TOKEN_KEY);
        
        return accessToken;
    }
    
    /**
     * Generate refresh_token to create a new access_token when the access_token expires.
     */
    public static String genRefreshToken(Long userId, String username) {
        HashMap<String, Object> refreshTokenPayload = new HashMap<>();
        refreshTokenPayload.put(SecurityConstant.USER_ID, userId);
        refreshTokenPayload.put(SecurityConstant.USERNAME, username);
        
        String refreshToken = JWTUtil.createToken(refreshTokenPayload, SecurityConstant.REFRESH_TOKEN_KEY);
        
        return refreshToken;
    }
}
