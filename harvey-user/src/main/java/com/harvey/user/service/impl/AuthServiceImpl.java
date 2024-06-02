package com.harvey.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.user.common.entity.domain.AuthDo;
import com.harvey.user.mapper.AuthMapper;
import com.harvey.user.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, AuthDo> implements AuthService {
}