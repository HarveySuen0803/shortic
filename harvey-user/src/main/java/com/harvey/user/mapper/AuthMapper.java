package com.harvey.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.user.common.model.po.AuthPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Mapper
public interface AuthMapper extends BaseMapper<AuthPo> {
}