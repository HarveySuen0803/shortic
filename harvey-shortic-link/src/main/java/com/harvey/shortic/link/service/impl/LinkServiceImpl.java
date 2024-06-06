package com.harvey.shortic.link.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.constant.Constant;
import com.harvey.common.exception.ClientException;
import com.harvey.common.exception.ServerException;
import com.harvey.common.result.Result;
import com.harvey.common.result.link.LinkResult;
import com.harvey.common.support.HashBase62Util;
import com.harvey.shortic.link.common.constant.LinkConstant;
import com.harvey.shortic.link.common.entity.dto.LinkAddDto;
import com.harvey.shortic.link.common.entity.dto.LinkPageDto;
import com.harvey.shortic.link.common.entity.dto.LinkSetDto;
import com.harvey.shortic.link.common.entity.po.LinkPo;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
import com.harvey.shortic.link.common.entity.vo.LinkPageVo;
import com.harvey.shortic.link.common.entity.vo.LinkVo;
import com.harvey.shortic.link.config.BloomFilterConfig;
import com.harvey.shortic.link.mapper.LinkMapper;
import com.harvey.shortic.link.service.LinkService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkPo> implements LinkService {
    @Resource(name = "shortUriBloomFilter")
    private RBloomFilter shortUriBloomFilter;
    
    @Resource
    private LinkMapper linkMapper;
    
    @Resource
    private HttpServletResponse response;
    
    @Override
    public String getLongUrl(String shortUri) {
        if (StrUtil.isBlank(shortUri)) {
            throw new ClientException(LinkResult.SHORT_URL_INVALID);
        }
        
        if (!shortUriBloomFilter.contains(shortUri)) {
            throw new ClientException(LinkResult.SHORT_URI_NOT_EXISTS);
        }
        
        LinkPo linkPo = lambdaQuery()
            .eq(LinkPo::getShortUri, shortUri)
            .one();
        if (linkPo == null) {
            throw new ClientException(LinkResult.SHORT_URI_NOT_EXISTS);
        }
        
        String longUrl = linkPo.getLongUrl();
        
        return longUrl;
    }
    
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
            .eq(LinkPo::getShortUri, shortUri)
            .exists();
        
        return isExists;
    }
    
    @Override
    public boolean isShortUriNotExists(String shortUri) {
        return !isShortUriExists(shortUri);
    }
    
    @Override
    public LinkPageVo pageLink(LinkPageDto linkPageDto) {
        String gid = linkPageDto.getGid();
        Long pageNo = linkPageDto.getPageNo();
        Long pageSize = linkPageDto.getPageSize();
        
        Page<LinkPo> page = new Page<>(pageNo, pageSize);
        
        lambdaQuery()
            .eq(LinkPo::getGid, gid)
            .eq(LinkPo::getDeletedFlag, Constant.NOT_DELETED)
            .page(page);
        
        List<LinkPo> linkPoList = page.getRecords();
        Long totalSize = page.getSize();
        
        List<LinkVo> linkVoList = linkPoList.stream()
            .map(linkPo -> BeanUtil.copyProperties(linkPo, LinkVo.class))
            .toList();
        
        LinkPageVo linkPageVo = new LinkPageVo();
        linkPageVo.setLinkVoList(linkVoList);
        linkPageVo.setTotalSize(totalSize);
        
        return linkPageVo;
    }
    
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
    
    @Transactional
    @Override
    public void addLink(LinkAddDto linkAddDto) {
        String longUrl = linkAddDto.getLongUrl();
        
        String shortUri = getShortUri(longUrl);
        
        String shortDom = linkAddDto.getShortDom();
        String shortUrl = shortDom + shortUri;
        
        LinkPo linkPo = BeanUtil.copyProperties(linkAddDto, LinkPo.class);
        linkPo.setShortUri(shortUri);
        linkPo.setShortUrl(shortUrl);
        
        saveOrUpdate(linkPo);
    }
    
    @Transactional
    @Override
    public void setLink(LinkSetDto linkSetDto) {
        String srcGid = linkSetDto.getSrcGid();
        String tarGid = linkSetDto.getTarGid();
        String shortUri = linkSetDto.getShortUri();
        
        boolean isShortUriNotExists = isShortUriNotExists(shortUri);
        if (isShortUriNotExists) {
            throw new ClientException(LinkResult.SHORT_URI_NOT_EXISTS);
        }
        
        shortUriBloomFilter.add(shortUri);
        
        LinkPo linkPo = lambdaQuery()
            .eq(LinkPo::getShortUri, shortUri)
            .eq(LinkPo::getGid, srcGid)
            .eq(LinkPo::getEnabledFlag, Constant.ENABLED)
            .eq(LinkPo::getDeletedFlag, Constant.NOT_DELETED)
            .one();
        if (ObjUtil.isNull(linkPo)) {
            throw new ClientException(LinkResult.LINK_NOT_EXISTS);
        }
        
        BeanUtil.copyProperties(linkSetDto, linkPo);
        linkPo.setGid(tarGid);
        
        saveOrUpdate(linkPo);
    }
}
