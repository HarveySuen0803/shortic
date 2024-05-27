package com.harvey.user.service;

import com.harvey.user.domain.UserDo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.user.vo.UserVo;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
public interface UserService extends IService<UserDo> {
    UserVo getUser(String username);
    
    boolean isUserExists(String username, String email);
    
    boolean isUsernameExists(String username);
    
    boolean isEmailExists(String email);
}