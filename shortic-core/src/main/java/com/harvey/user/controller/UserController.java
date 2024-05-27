package com.harvey.user.controller;

import cn.hutool.core.date.DateTime;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.Result;
import com.harvey.user.domain.UserDo;
import com.harvey.user.dto.UserRegisterDto;
import com.harvey.user.mapper.UserMapper;
import com.harvey.user.result.UserResult;
import com.harvey.user.service.UserService;
import com.harvey.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@RestController
@RequestMapping("/api/user/v1")
@PreAuthorize("hasAuthority('USER_R')")
@RequiredArgsConstructor
@EnableMethodSecurity
public class UserController {
    private final UserService userService;
    
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public Result<Void> register(UserRegisterDto userRegisterDto) {
        boolean isUserExist = userService.isUserExists(userRegisterDto.getUsername(), userRegisterDto.getUsername());
        if (isUserExist) {
            throw new ClientException(UserResult.USER_EXISTS);
        }
        
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userRegisterDto, userDo);
        
        String password = userDo.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userDo.setPassword(encodedPassword);
        
        userService.save(userDo);
        
        return Result.success();
    }

    @GetMapping("/{username}")
    public Result<UserVo> getUser(@PathVariable String username) {
        UserVo userVo = userService.getUser(username);
        
        return Result.success(userVo);
    }
    
    @GetMapping("/{username}/mask")
    public Result<UserVo> getUserMask(@PathVariable String username) {
        UserVo userVo = userService.getUser(username);
        
        userVo.mask();
        
        return Result.success(userVo);
    }
}