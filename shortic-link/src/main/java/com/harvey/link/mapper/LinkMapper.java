package com.harvey.link.mapper;

import com.harvey.link.entitiy.domain.LinkDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Mapper
public interface LinkMapper extends BaseMapper<LinkDo> {
    @MapKey("gid")
    List<Map<String, Object>> countLink(@Param("gidList") List<String> gidList);
}
