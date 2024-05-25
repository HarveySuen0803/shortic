package com.harvey.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.user.domain.UserAuthDo;
import com.harvey.user.mapper.UserAuthMapper;
import com.harvey.user.service.UserAuthService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuthDo> implements UserAuthService {
}