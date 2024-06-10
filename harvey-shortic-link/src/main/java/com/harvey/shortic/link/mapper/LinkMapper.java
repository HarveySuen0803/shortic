package com.harvey.shortic.link.mapper;

import com.harvey.shortic.link.common.model.po.LinkPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Mapper
public interface LinkMapper extends BaseMapper<LinkPo> {
    @MapKey("gid")
    List<Map<String, Object>> countLink(@Param("gidList") List<String> gidList);
}
