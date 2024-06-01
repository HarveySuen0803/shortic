package com.harvey.user.support;

import com.harvey.common.result.Result;
import com.harvey.common.support.ResponseUtil;
import com.harvey.security.support.AuthenticationTokenUtil;
import com.harvey.user.common.UserCacheKey;
import com.harvey.user.entity.domain.UserDo;
import com.harvey.user.entity.vo.LoginVo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-01
 */
@Component
public class GlobalAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private RedisTemplate redisTemplate;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDo userDo = (UserDo) authentication.getPrincipal();
        Long userId = userDo.getId();
        String username = userDo.getUsername();
        String password = userDo.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDo.getAuthorities();
        
        String accessToken = AuthenticationTokenUtil.genAccessToken(userId, username, password, authorities);
        
        redisTemplate.opsForValue().set(
            UserCacheKey.ACCESS_TOKEN.getKey(userId),
            accessToken,
            UserCacheKey.ACCESS_TOKEN.timeout,
            UserCacheKey.ACCESS_TOKEN.unit
        );
        
        String refreshToken = AuthenticationTokenUtil.genRefreshToken(userId, username);
        redisTemplate.opsForValue().set(
            UserCacheKey.REFRESH_TOKEN.getKey(userId),
            refreshToken
        );
        
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(userId);
        loginVo.setUsername(username);
        loginVo.setAccessToken(accessToken);
        loginVo.setRefreshToken(refreshToken);
        Result<LoginVo> loginVoResult = Result.success(loginVo);
        
        ResponseUtil.write(response, loginVoResult);
    }
}
