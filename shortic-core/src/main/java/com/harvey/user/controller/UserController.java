package com.harvey.user.controller;

import com.harvey.convention.result.Result;
import com.harvey.user.service.UserService;
import com.harvey.user.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{name}")
    public Result<UserVo> getUserByName(@PathVariable String name) {
        UserVo userVo = userService.getUserByName(name);
        
        return Result.success(userVo);
    }
    
    @GetMapping("/{name}/mask")
    public Result<UserVo> getUserMaskByName(@PathVariable String name) {
        UserVo userVo = userService.getUserByName(name);
        
        userVo.mask();
        
        return Result.success(userVo);
    }
}