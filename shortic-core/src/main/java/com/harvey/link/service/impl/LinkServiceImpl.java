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
}
