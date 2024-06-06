package com.harvey.shortic.link.rpc.service;

import com.harvey.shortic.link.common.entity.dto.LinkSetDto;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
public interface LinkRpcService {
    List<LinkGroupCountVo> countLink(List<String> gidList);
    
    void setLink(LinkSetDto linkSetDto);
}
