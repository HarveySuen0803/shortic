package com.harvey.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.user.domain.RoleDo;
import com.harvey.user.domain.UserDo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDo> {
}