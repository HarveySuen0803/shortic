package com.harvey.shortic.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.ServerException;
import com.harvey.common.result.link.LinkResult;
import com.harvey.common.support.HashBase62Util;
import com.harvey.rpc.link.entity.dto.LinkGroupCountDto;
import com.harvey.rpc.link.service.RpcLinkService;
import com.harvey.shortic.link.common.LinkConstant;
import com.harvey.shortic.link.entitiy.domain.LinkDo;
import com.harvey.shortic.link.mapper.LinkMapper;
import com.harvey.shortic.link.service.LinkService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Service
@DubboService
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDo> implements LinkService, RpcLinkService {
    @Resource(name = "shortUriBloomFilter")
    private RBloomFilter shortUriBloomFilter;
    
    @Resource
    private LinkMapper linkMapper;
    
    @Override
    public String getShortUri(String longUrl) {
        String shortUri = HashBase62Util.toBase62(longUrl, 6);
        
        int shortUriGenerateCount = 0;
        
        // Continue generating new short URIs while the current short URI exists in the Bloom filter
        while (shortUriBloomFilter.contains(shortUri)) {
            // If the generation attempt exceeds the maximum allowed count, handle the failure
            if (shortUriGenerateCount > LinkConstant.MAX_SHORT_URI_GENERATE_COUNT) {
                // Check if the short URI does not exist in the actual storage
                if (isShortUriNotExists(shortUri)) {
                    break;
                }
                // Throw a server exception indicating short URI generation failure
                throw new ServerException(LinkResult.SHORT_URI_GENERATE_FAILURE);
            }
            
            // Continue generating new short URIs while the current short URI exists in the Bloom filter
            longUrl += UUID.randomUUID().toString();
            
            // Generate a new base62 short URI with a length of 6 from the modified long URL
            shortUri = HashBase62Util.toBase62(longUrl, 6);
            
            shortUriGenerateCount++;
        }
        
        // Increment the short URI generation attempt counter
        shortUriBloomFilter.add(shortUri);
        
        return shortUri;
    }
    
    @Override
    public boolean isShortUriExists(String shortUri) {
        boolean isExists = lambdaQuery()
            .eq(LinkDo::getShortUri, shortUri)
            .exists();
        
        return isExists;
    }
    
    @Override
    public boolean isShortUriNotExists(String shortUri) {
        return !isShortUriExists(shortUri);
    }
    
    @Override
    public List<LinkGroupCountDto> countLink(List<String> gidList) {
        List<Map<String, Object>> linkDoMapList = linkMapper.countLink(gidList);
        
        List<LinkGroupCountDto> linkGroupCountDtoList = linkDoMapList.stream()
            .map(linkDoMap -> {
                LinkGroupCountDto linkGroupCountDto = new LinkGroupCountDto();
                linkGroupCountDto.setGid((String) linkDoMap.get("gid"));
                linkGroupCountDto.setCount((Long) linkDoMap.get("gid_cnt"));
                return linkGroupCountDto;
            })
            .toList();
        
        return linkGroupCountDtoList;
    }
}
