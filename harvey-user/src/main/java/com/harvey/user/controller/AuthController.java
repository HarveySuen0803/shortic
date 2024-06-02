package com.harvey.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.Result;
import com.harvey.user.common.constant.UserCacheKey;
import com.harvey.user.common.constant.UserConstant;
import com.harvey.user.common.constant.UserResult;
import com.harvey.user.common.entity.domain.UserDo;
import com.harvey.user.common.entity.dto.UserRegisterDto;
import com.harvey.user.common.entity.vo.UserRefreshTokenVo;
import com.harvey.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RBloomFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-02
 */
@RestController
public class AuthController {
    @Resource
    private UserService userService;
    
    @Resource
    private UserDetailsService userDetailsService;
    
    @Resource
    private PasswordEncoder passwordEncoder;
    
    @Resource
    private HttpServletRequest request;
    
    @Resource
    private RedisTemplate redisTemplate;
    
    @Resource(name = "userBloomFilter")
    private RBloomFilter userBloomFilter;
    
    @Transactional
    @PostMapping(path = "/api/user/v1/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result<Void> register(@ModelAttribute UserRegisterDto userRegisterDto) {
        boolean isUserExist = userService.isUserExists(userRegisterDto.getUsername(), userRegisterDto.getEmail());
        if (isUserExist) {
            throw new ClientException(UserResult.USER_NOT_FOUND);
        }
        
        UserDo userDo = BeanUtil.copyProperties(userRegisterDto, UserDo.class);
        
        String password = userDo.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userDo.setPassword(encodedPassword);
        
        userService.saveOrUpdate(userDo);
        
        userBloomFilter.add(userDo.getId());
        
        return Result.success();
    }
    
    @PostMapping(path = "/api/user/v1/login/refresh", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result<UserRefreshTokenVo> refreshToken(@RequestParam String refreshToken) {
        // If the request header does not carry Login Token, deny access directly.
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(accessToken)) {
            throw new ClientException(Result.UNAUTHORIZED);
        }
        
        // If the access_token is incorrect, deny access directly.
        JWT accessTokenJwt = JWT.of(accessToken).setKey(UserConstant.ACCESS_TOKEN_KEY);
        if (!accessTokenJwt.verify()) {
            throw new ClientException(Result.UNAUTHORIZED);
        }
        
        // If the refresh_token is blank, deny access directly.
        if (StrUtil.isBlank(refreshToken)) {
            throw new ClientException(Result.UNAUTHORIZED);
        }
        
        // If the refresh_token is incorrect, deny access directly.
        JWT refreshTokenJwt = JWT.of(refreshToken).setKey(UserConstant.REFRESH_TOKEN_KEY);
        if (!refreshTokenJwt.verify()) {
            throw new ClientException(Result.UNAUTHORIZED);
        }
        
        String username = refreshTokenJwt.getPayload(UserConstant.USERNAME).toString();
        Long userId = Long.valueOf(refreshTokenJwt.getPayload(UserConstant.USER_ID).toString());
        
        // If the access_token is inconsistent with the access_token stored in Redis, deny access directly.
        String accessTokenCacheKey = UserCacheKey.ACCESS_TOKEN.getKey(userId);
        String accessTokenCache = (String) redisTemplate.opsForValue().get(accessTokenCacheKey);
        if (!accessToken.equals(accessTokenCache)) {
            throw new ClientException(Result.UNAUTHORIZED);
        }
        
        // Get the latest password and permission information from the database.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String password = userDetails.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        
        accessToken = userService.buildAccessTokenCache(userId, username, password, authorities);
        
        UserRefreshTokenVo userRefreshTokenVo = new UserRefreshTokenVo();
        userRefreshTokenVo.setAccessToken(accessToken);
        
        return Result.success(userRefreshTokenVo);
    }
}