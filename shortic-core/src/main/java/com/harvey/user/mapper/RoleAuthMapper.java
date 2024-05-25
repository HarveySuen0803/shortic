package com.harvey.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.user.domain.RoleAuthDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleAuthMapper extends BaseMapper<RoleAuthDo> {
}