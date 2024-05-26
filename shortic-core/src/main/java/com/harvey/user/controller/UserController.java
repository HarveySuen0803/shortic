package com.harvey.user.controller;

import com.harvey.common.result.Result;
import com.harvey.user.service.UserService;
import com.harvey.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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