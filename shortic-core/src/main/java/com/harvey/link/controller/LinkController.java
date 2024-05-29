package com.harvey.link.controller;

import cn.hutool.core.bean.BeanUtil;
import com.harvey.common.constant.Result;
import com.harvey.common.util.HashBase62Util;
import com.harvey.link.entitiy.domain.LinkDo;
import com.harvey.link.entitiy.dto.LinkAddDto;
import com.harvey.link.service.LinkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@RestController
public class LinkController {
    @Resource
    private LinkService linkService;
    
    @PutMapping("/api/shortic/link/v1")
    public Result<Void> addLink(@RequestBody LinkAddDto linkAddDto) {
        String longUrl = linkAddDto.getLongUrl();
        
        String shortUri = linkService.getShortUri(longUrl);
        
        String shortDim = linkAddDto.getShortDim();
        String shortUrl = shortDim + shortUri;
        
        LinkDo linkDo = BeanUtil.copyProperties(linkAddDto, LinkDo.class);
        linkDo.setShortUri(shortUri);
        linkDo.setShortUrl(shortUrl);
        
        linkService.saveOrUpdate(linkDo);
        
        return Result.success();
    }
}
