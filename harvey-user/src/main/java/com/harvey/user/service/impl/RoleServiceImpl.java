package com.harvey.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.user.common.entity.domain.RoleDo;
import com.harvey.user.mapper.RoleMapper;
import com.harvey.user.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDo> implements RoleService {
}