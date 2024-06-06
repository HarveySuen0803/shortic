package com.harvey.shortic.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.shortic.link.common.entity.dto.LinkAddDto;
import com.harvey.shortic.link.common.entity.dto.LinkPageDto;
import com.harvey.shortic.link.common.entity.dto.LinkSetDto;
import com.harvey.shortic.link.common.entity.po.LinkPo;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
import com.harvey.shortic.link.common.entity.vo.LinkPageVo;
import org.springframework.web.bind.annotation.PathVariable;

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
