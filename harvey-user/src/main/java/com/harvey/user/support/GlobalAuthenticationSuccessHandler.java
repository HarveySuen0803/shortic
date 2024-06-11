package com.harvey.user.support;

import com.harvey.common.constant.Result;
import com.harvey.common.support.ResponseUtil;
import com.harvey.security.model.dto.UserDetailsDto;
import com.harvey.security.support.AuthenticationTokenUtil;
import com.harvey.security.common.constant.SecurityCacheKey;
import com.harvey.user.common.model.vo.LoginVo;
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
        UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();
        Long userId = userDetailsDto.getId();
        String username = userDetailsDto.getUsername();
        String password = userDetailsDto.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDetailsDto.getAuthorities();
        
        String accessToken = AuthenticationTokenUtil.genAccessToken(userId, username, password, authorities);
        
        redisTemplate.opsForValue().set(
            SecurityCacheKey.ACCESS_TOKEN.getKey(userId),
            accessToken,
            SecurityCacheKey.ACCESS_TOKEN.timeout,
            SecurityCacheKey.ACCESS_TOKEN.unit
        );
        
        String refreshToken = AuthenticationTokenUtil.genRefreshToken(userId, username);
        redisTemplate.opsForValue().set(
            SecurityCacheKey.REFRESH_TOKEN.getKey(userId),
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
