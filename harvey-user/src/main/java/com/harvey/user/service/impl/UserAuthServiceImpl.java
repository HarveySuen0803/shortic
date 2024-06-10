package com.harvey.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.user.common.model.po.UserAuthPo;
import com.harvey.user.mapper.UserAuthMapper;
import com.harvey.user.service.UserAuthService;
import org.springframework.stereotype.Service;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuthPo> implements UserAuthService {
}