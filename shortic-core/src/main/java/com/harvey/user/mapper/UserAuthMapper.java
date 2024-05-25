package com.harvey.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.user.domain.UserAuthDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuthDo> {
}
