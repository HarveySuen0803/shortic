package com.harvey.user.controller;

import cn.hutool.core.util.ObjUtil;
import com.harvey.convention.exception.ClientException;
import com.harvey.convention.result.Result;
import com.harvey.convention.exception.ServerException;
import com.harvey.user.common.UserResultStatus;
import com.harvey.user.domain.UserDo;
import com.harvey.user.service.UserService;
import com.harvey.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
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
        UserDo userDo = userService.lambdaQuery()
            .select(UserDo::getName, UserDo::getPhone, UserDo::getEmail)
            .eq(UserDo::getName, name)
            .one();
        if (ObjUtil.isNull(userDo)) {
            throw new ClientException(UserResultStatus.USER_NOT_FOUND);
        }
        
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDo, userVo);
        
        return Result.success(userVo);
    }
}