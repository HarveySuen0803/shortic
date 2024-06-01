package com.harvey.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWT;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.Result;
import com.harvey.user.common.UserConstant;
import com.harvey.user.common.UserResult;
import com.harvey.user.entity.domain.UserDo;
import com.harvey.user.entity.dto.UserRegisterDto;
import com.harvey.user.entity.vo.UserRefreshTokenVo;
import com.harvey.user.entity.vo.UserVo;
import com.harvey.user.service.UserService;
import com.harvey.security.support.AuthenticationTokenUtil;
import jakarta.annotation.Resource;
import org.redisson.api.RBloomFilter;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@RestController
@PreAuthorize("hasAuthority('USER_R')")
@EnableMethodSecurity
public class UserController {
    @Resource
    private UserService userService;
    
    @Resource
    private PasswordEncoder passwordEncoder;
    
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
        JWT refreshTokenJwt = JWT.of(refreshToken).setKey(UserConstant.REFRESH_TOKEN_KEY);
        
        if (!refreshTokenJwt.verify()) {
            throw new ClientException(Result.FORBIDDEN);
        }
        
        String username = refreshTokenJwt.getPayload(UserConstant.USERNAME).toString();
        
        UserDo userDo = userService.getUserDo(username);
        Long userId = userDo.getId();
        String password = userDo.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDo.getAuthorities();
        
        String accessToken = AuthenticationTokenUtil.genAccessToken(userId, username, password, authorities);
        
        UserRefreshTokenVo userRefreshTokenVo = new UserRefreshTokenVo();
        userRefreshTokenVo.setAccessToken(accessToken);
        
        return Result.success(userRefreshTokenVo);
    }

    @GetMapping("/api/user/v1/{username}")
    public Result<UserVo> getUser(@PathVariable String username) {
        UserVo userVo = userService.getUserVo(username);
        
        return Result.success(userVo);
    }
    
    @GetMapping("/api/user/v1/{username}/mask")
    public Result<UserVo> getUserMask(@PathVariable String username) {
        UserVo userVo = userService.getUserVo(username);
        
        userVo.mask();
        
        return Result.success(userVo);
    }
}
