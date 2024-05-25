package com.harvey.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.user.domain.AuthDo;
import com.harvey.user.mapper.AuthMapper;
import com.harvey.user.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, AuthDo> implements AuthService {
}