package com.harvey.shortic.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.shortic.link.common.model.dto.LinkAddDto;
import com.harvey.shortic.link.common.model.dto.LinkPageDto;
import com.harvey.shortic.link.common.model.dto.LinkSetDto;
import com.harvey.shortic.link.common.model.po.LinkPo;
import com.harvey.shortic.link.common.model.vo.LinkGroupCountVo;
import com.harvey.shortic.link.common.model.vo.LinkPageVo;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public interface LinkService extends IService<LinkPo> {
    String getLongUrl(String shortUri);
    
    LinkPageVo pageLink(LinkPageDto linkPageDto);
    
    String getShortUri(String longUrl);
    
    boolean isShortUriExists(String shortUri);
    
    boolean isShortUriNotExists(String shortUri);
    
    List<LinkGroupCountVo> countLink(List<String> gidList);
    
    void addLink(LinkAddDto linkAddDto);
    
    void setLink(LinkSetDto linkSetDto);
}
