package com.harvey.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.Result;
import com.harvey.user.domain.UserDo;
import com.harvey.user.dto.UserRegisterDto;
import com.harvey.user.result.UserResult;
import com.harvey.user.service.UserService;
import com.harvey.user.vo.UserVo;
import jakarta.annotation.Resource;
import org.redisson.api.RBloomFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    
    @PostMapping("/api/user/v1/register")
    public Result<Void> register(UserRegisterDto userRegisterDto) {
        boolean isUserExist = userService.isUserExists(userRegisterDto.getUsername(), userRegisterDto.getEmail());
        if (isUserExist) {
            throw new ClientException(UserResult.USER_EXISTS);
        }
        
        UserDo userDo = BeanUtil.copyProperties(userRegisterDto, UserDo.class);
        
        String password = userDo.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userDo.setPassword(encodedPassword);
        
        userService.save(userDo);
        
        System.out.println(userDo);
        
        userBloomFilter.add(userDo.getId());
        
        return Result.success();
    }

    @GetMapping("/api/user/v1/{username}")
    public Result<UserVo> getUser(@PathVariable String username) {
        UserVo userVo = userService.getUser(username);
        
        return Result.success(userVo);
    }
    
    @GetMapping("/api/user/v1/{username}/mask")
    public Result<UserVo> getUserMask(@PathVariable String username) {
        UserVo userVo = userService.getUser(username);
        
        userVo.mask();
        
        return Result.success(userVo);
    }
}
