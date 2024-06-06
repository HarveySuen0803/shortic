package com.harvey.shortic.link.service.impl;

import com.harvey.shortic.link.common.entity.dto.LinkAddDto;
import com.harvey.shortic.link.common.entity.dto.LinkSetDto;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
import com.harvey.shortic.link.mapper.LinkMapper;
import com.harvey.shortic.link.rpc.service.LinkRpcService;
import com.harvey.shortic.link.service.LinkService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-05
 */
@Service
@DubboService(parameters = {"serialization", "fastjson2"})
public class LinkRpcServiceImpl implements LinkRpcService {
    @Resource
    private LinkService linkService;
    
    @Override
    public List<LinkGroupCountVo> countLink(List<String> gidList) {
        List<LinkGroupCountVo> linkGroupCountVoList = linkService.countLink(gidList);
        
        return linkGroupCountVoList;
    }
    
    @Override
    public void addLink(LinkAddDto linkAddDto) {
        linkService.addLink(linkAddDto);
    }
    
    @Override
    public void setLink(LinkSetDto linkSetDto) {
        linkService.setLink(linkSetDto);
    }
}
