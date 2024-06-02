package com.harvey.shortic.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.shortic.link.common.entity.domain.LinkDo;
import com.harvey.shortic.link.common.entity.dto.LinkPageDto;
import com.harvey.shortic.link.common.entity.vo.LinkPageVo;
import com.harvey.shortic.link.common.entity.dto.LinkAddDto;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public interface LinkService extends IService<LinkDo> {
    void addLink(LinkAddDto linkAddDto);
    
    LinkPageVo pageLink(LinkPageDto linkPageDto);
    
    String getShortUri(String longUrl);
    
    boolean isShortUriExists(String shortUri);
    
    boolean isShortUriNotExists(String shortUri);
    
    List<LinkGroupCountVo> countLink(List<String> gidList);
}
