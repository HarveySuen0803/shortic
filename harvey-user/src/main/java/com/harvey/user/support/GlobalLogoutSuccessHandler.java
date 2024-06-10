package com.harvey.user.support;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.harvey.common.constant.Result;
import com.harvey.common.support.ResponseUtil;
import com.harvey.security.constant.SecurityCacheKey;
import com.harvey.security.constant.SecurityConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-01
 */
@Component
public class GlobalLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    private RedisTemplate redisTemplate;
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // If the request header does not carry Login Token.
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(accessToken)) {
            ResponseUtil.write(response, Result.UNAUTHORIZED);
            return;
        }
        
        // If the access_token is incorrect, deny access directly.
        JWT accessTokenJwt = JWT.of(accessToken).setKey(SecurityConstant.ACCESS_TOKEN_KEY);
        if (!accessTokenJwt.verify()) {
            ResponseUtil.write(response, Result.UNAUTHORIZED);
            return;
        }
        
        // If the access_token expires, notify the client to obtain a new access_token.
        if (!accessTokenJwt.validate(0)) {
            ResponseUtil.write(response, Result.TOKEN_EXPIRED);
            return;
        }
        
        // If the access_token is inconsistent with the access_token stored in Redis,
        // notify the client to obtain a new access_token.
        Long userId = Long.valueOf(accessTokenJwt.getPayload(SecurityConstant.USER_ID).toString());
        String loginTokenCacheKey = SecurityCacheKey.ACCESS_TOKEN.getKey(userId);
        String loginTokenCache = (String) redisTemplate.opsForValue().get(loginTokenCacheKey);
        if (!accessToken.equals(loginTokenCache)) {
            ResponseUtil.write(response, Result.TOKEN_EXPIRED);
            return;
        }
        
        redisTemplate.delete(SecurityCacheKey.ACCESS_TOKEN.getKey(userId));
        redisTemplate.delete(SecurityCacheKey.REFRESH_TOKEN.getKey(userId));
        ResponseUtil.write(response, Result.SUCCESS);
    }
}
