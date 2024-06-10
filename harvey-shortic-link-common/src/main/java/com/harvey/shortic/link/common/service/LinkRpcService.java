package com.harvey.shortic.link.common.service;

import com.harvey.shortic.link.common.model.dto.LinkAddDto;
import com.harvey.shortic.link.common.model.dto.LinkSetDto;
import com.harvey.shortic.link.common.model.vo.LinkGroupCountVo;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
public interface LinkRpcService {
    String getLongUrl(String shortUri);
    
    List<LinkGroupCountVo> countLink(List<String> gidList);
    
    void addLink(LinkAddDto linkAddDto);
    
    void setLink(LinkSetDto linkSetDto);
}
