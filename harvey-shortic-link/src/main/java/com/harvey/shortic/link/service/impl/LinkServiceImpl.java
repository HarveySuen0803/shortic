package com.harvey.shortic.link.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.constant.Constant;
import com.harvey.common.exception.ServerException;
import com.harvey.common.result.link.LinkResult;
import com.harvey.common.support.HashBase62Util;
import com.harvey.shortic.link.common.constant.LinkConstant;
import com.harvey.shortic.link.common.entity.domain.LinkDo;
import com.harvey.shortic.link.common.entity.vo.LinkVo;
import com.harvey.shortic.link.mapper.LinkMapper;
import com.harvey.shortic.link.common.entity.dto.LinkAddDto;
import com.harvey.shortic.link.common.entity.dto.LinkPageDto;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
import com.harvey.shortic.link.common.entity.vo.LinkPageVo;
import com.harvey.shortic.link.rpc.service.LinkRpcService;
import com.harvey.shortic.link.service.LinkService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDo> implements LinkService, LinkRpcService {
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
    
    @Transactional
    @Override
    public void addLink(LinkAddDto linkAddDto) {
        String longUrl = linkAddDto.getLongUrl();
        
        String shortUri = getShortUri(longUrl);
        
        String shortDim = linkAddDto.getShortDim();
        String shortUrl = shortDim + shortUri;
        
        LinkDo linkDo = BeanUtil.copyProperties(linkAddDto, LinkDo.class);
        linkDo.setShortUri(shortUri);
        linkDo.setShortUrl(shortUrl);
        
        saveOrUpdate(linkDo);
    }
    
    @Override
    public LinkPageVo pageLink(LinkPageDto linkPageDto) {
        String gid = linkPageDto.getGid();
        Long pageNo = linkPageDto.getPageNo();
        Long pageSize = linkPageDto.getPageSize();
        
        Page<LinkDo> page = new Page<>(pageNo, pageSize);
        
        lambdaQuery()
            .eq(LinkDo::getGid, gid)
            .eq(LinkDo::getIsDeleted, Constant.NOT_DELETED)
            .eq(LinkDo::getIsEnabled, Constant.ENABLED)
            .page(page);
        
        List<LinkDo> linkDoList = page.getRecords();
        Long totalSize = page.getSize();
        
        List<LinkVo> linkVoList = linkDoList.stream()
            .map(linkDo -> BeanUtil.copyProperties(linkDo, LinkVo.class))
            .toList();
        
        LinkPageVo linkPageVo = new LinkPageVo();
        linkPageVo.setLinkVoList(linkVoList);
        linkPageVo.setTotalSize(totalSize);
        
        return linkPageVo;
    }
    
    @Override
    public List<LinkGroupCountVo> countLink(List<String> gidList) {
        List<Map<String, Object>> linkDoMapList = linkMapper.countLink(gidList);
        
        List<LinkGroupCountVo> linkGroupCountVoList = linkDoMapList.stream()
            .map(linkDoMap -> {
                LinkGroupCountVo linkGroupCountVo = new LinkGroupCountVo();
                linkGroupCountVo.setGid((String) linkDoMap.get("gid"));
                linkGroupCountVo.setCount((Long) linkDoMap.get("gid_cnt"));
                return linkGroupCountVo;
            })
            .toList();
        
        return linkGroupCountVoList;
    }
}
