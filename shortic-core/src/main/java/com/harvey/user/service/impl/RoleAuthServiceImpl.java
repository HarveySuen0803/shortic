package com.harvey.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.user.domain.RoleAuthDo;
import com.harvey.user.mapper.RoleAuthMapper;
import com.harvey.user.service.RoleAuthService;
import org.springframework.stereotype.Service;

@Service
public class RoleAuthServiceImpl extends ServiceImpl<RoleAuthMapper, RoleAuthDo> implements RoleAuthService {
}