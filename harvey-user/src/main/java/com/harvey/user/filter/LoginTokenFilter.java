package com.harvey.user.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.harvey.common.constant.CacheKey;
import com.harvey.common.constant.Constant;
import com.harvey.common.result.Result;
import com.harvey.common.support.ResponseUtil;
import com.harvey.user.support.UserContext;
import com.harvey.user.support.UserContextHolder;
import com.harvey.user.common.UserHttpUri;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Component
public class LoginTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisTemplate redisTemplate;
    
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // If the request is login, then skip the filter.
        String requestUri = request.getRequestURI();
        if (StrUtil.equals(requestUri, UserHttpUri.LOGIN)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // If the request header does not carry Login Token.
        String loginToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(loginToken)) {
            ResponseUtil.write(response, Result.unauthorized());
            return;
        }
        
        // If the token is invalid or expired.
        JWT loginTokenJwt = JWT.of(loginToken).setKey(Constant.LOGIN_TOKEN_KEY);
        if (!loginTokenJwt.verify() || !loginTokenJwt.validate(0)) {
            ResponseUtil.write(response, Result.unauthorized());
            return;
        }
        
        // Get user info from token.
        Long userId = Long.valueOf(loginTokenJwt.getPayload(Constant.USER_ID).toString());
        String username = loginTokenJwt.getPayload(Constant.USERNAME).toString();
        String password = loginTokenJwt.getPayload(Constant.PASSWORD).toString();
        String authoritiesJson = loginTokenJwt.getPayload(Constant.AUTHORITIES).toString();
        Collection<? extends GrantedAuthority> authorities = JSON.parseObject(authoritiesJson, new TypeReference<>() {});
        
        // Set user info to UserContext
        UserContext userContext = new UserContext(userId, username, authorities);
        UserContextHolder.setUserContext(userContext);
        
        // Determine if the token is correct
        String loginTokenCacheKey = CacheKey.LOGIN_TOKEN.getKey(userId);
        String loginTokenCache = (String) redisTemplate.opsForValue().get(loginTokenCacheKey);
        if (!loginToken.equals(loginTokenCache)) {
            ResponseUtil.write(response, Result.UNAUTHORIZED);
            return;
        }
        
        // Renew the token
        threadPoolTaskExecutor.execute(() -> {
            redisTemplate.expire(loginTokenCacheKey, CacheKey.LOGIN_TOKEN.timeout, CacheKey.LOGIN_TOKEN.unit);
        });
        
        // If the user is not authenticated, then authenticate the user.
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(username, password, authorities));
        }
        
        filterChain.doFilter(request, response);
    }
}