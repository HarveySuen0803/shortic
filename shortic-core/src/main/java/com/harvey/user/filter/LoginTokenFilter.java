package com.harvey.user.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.alibaba.fastjson2.JSON;
import com.harvey.common.util.ResponseUtil;
import com.harvey.common.constant.Result;
import com.harvey.user.constant.UserCacheKey;
import com.harvey.user.constant.UserConstant;
import com.harvey.user.entity.domain.UserDo;
import com.harvey.user.constant.UserHttpUri;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        String loginTokenStr = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(loginTokenStr)) {
            ResponseUtil.write(response, Result.UNAUTHORIZED);
            return;
        }
        
        // If the token is invalid or expired.
        JWT loginTokenJwt = JWT.of(loginTokenStr).setKey(UserConstant.LOGIN_TOKEN_KEY);
        if (!loginTokenJwt.verify() || !loginTokenJwt.validate(0)) {
            ResponseUtil.write(response, Result.UNAUTHORIZED);
            return;
        }
        
        // Get user info from token.
        String userDetailsJson = loginTokenJwt.getPayload(UserConstant.USER_DETAILS_KEY).toString();
        UserDo userDetails = JSON.parseObject(userDetailsJson, UserDo.class);
        
        Long userId = userDetails.getId();
        
        String loginTokenCacheKey = UserCacheKey.LOGIN_TOKEN.getKey(userId);
        
        String loginTokenStrCache = (String) redisTemplate.opsForValue().get(loginTokenCacheKey);
        if (!loginTokenStr.equals(loginTokenStrCache)) {
            ResponseUtil.write(response, Result.UNAUTHORIZED);
            return;
        }
        
        // Todo: Optimize token renewal strategy
        threadPoolTaskExecutor.execute(() -> {
            redisTemplate.expire(loginTokenCacheKey, UserCacheKey.LOGIN_TOKEN.timeout, UserCacheKey.LOGIN_TOKEN.unit);
        });
        
        // If the user is not authenticated, then authenticate the user.
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        }
        
        filterChain.doFilter(request, response);
    }
}