package com.harvey.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.Result;
import com.harvey.user.entity.domain.UserDo;
import com.harvey.user.entity.dto.UserRegisterDto;
import com.harvey.user.entity.vo.UserVo;
import com.harvey.user.result.UserResult;
import com.harvey.user.service.UserService;
import jakarta.annotation.Resource;
import org.redisson.api.RBloomFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/api/user/v1/register")
    public Result<Void> register(UserRegisterDto userRegisterDto) {
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
