package com.harvey.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.convention.exception.ClientException;
import com.harvey.user.common.UserResultStatus;
import com.harvey.user.domain.UserDo;
import com.harvey.user.service.UserService;
import com.harvey.user.mapper.UserMapper;
import com.harvey.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDo> implements UserService {
    @Override
    public UserVo getUserByName(String name) {
        UserDo userDo = lambdaQuery()
            .select(UserDo::getName, UserDo::getPhone, UserDo::getEmail)
            .eq(UserDo::getName, name)
            .one();
        if (ObjUtil.isNull(userDo)) {
            throw new ClientException(UserResultStatus.USER_NOT_FOUND);
        }
        
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDo, userVo);
        
        return userVo;
    }
}