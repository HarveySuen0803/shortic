package com.harvey.shortic.link.service.impl;

import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
import com.harvey.shortic.link.mapper.LinkMapper;
import com.harvey.shortic.link.rpc.service.LinkRpcService;
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
    private LinkMapper linkMapper;
    
    @Override
    public List<LinkGroupCountVo> countLink(List<String> gidList) {
        List<Map<String, Object>> linkPoMapList = linkMapper.countLink(gidList);
        
        List<LinkGroupCountVo> linkGroupCountVoList = linkPoMapList.stream()
            .map(linkPoMap -> {
                LinkGroupCountVo linkGroupCountVo = new LinkGroupCountVo();
                linkGroupCountVo.setGid((String) linkPoMap.get("gid"));
                linkGroupCountVo.setLinkCount((long) linkPoMap.get("gid_cnt"));
                return linkGroupCountVo;
            })
            .toList();
        
        return linkGroupCountVoList;
    }
}
