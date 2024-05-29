package com.harvey.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.ServerException;
import com.harvey.common.util.HashBase62Util;
import com.harvey.link.constant.LinkConstant;
import com.harvey.link.constant.LinkResult;
import com.harvey.link.entitiy.domain.LinkDo;
import com.harvey.link.mapper.LinkMapper;
import com.harvey.link.service.LinkService;
import jakarta.annotation.Resource;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDo> implements LinkService {
    @Resource(name = "shortUriBloomFilter")
    private RBloomFilter shortUriBloomFilter;
    
    @Override
    public String getShortUri(String longUrl) {
        String shortUri = HashBase62Util.toBase62(longUrl, 6);
        
        int shortUriGenerateCount = 0;
        
        while (isShortUriExists(shortUri)) {
            if (shortUriGenerateCount > LinkConstant.MAX_SHORT_URI_GENERATE_COUNT) {
                throw new ServerException(LinkResult.SHORT_URI_GENERATE_FAILURE);
            }
            longUrl += UUID.randomUUID().toString();
            shortUri = HashBase62Util.toBase62(longUrl, 6);
            shortUriGenerateCount++;
        }
        
        shortUriBloomFilter.add(shortUri);
        
        return shortUri;
    }
    
    @Override
    public boolean isShortUriExists(String shortUri) {
        return shortUriBloomFilter.contains(shortUri);
    }
}
