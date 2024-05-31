package com.harvey.link.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.constant.Constant;
import com.harvey.common.constant.Result;
import com.harvey.link.entitiy.domain.LinkDo;
import com.harvey.link.entitiy.dto.LinkAddDto;
import com.harvey.link.entitiy.dto.LinkPageDto;
import com.harvey.link.entitiy.vo.LinkGroupCountVo;
import com.harvey.link.entitiy.vo.LinkPageVo;
import com.harvey.link.entitiy.vo.LinkVo;
import com.harvey.link.mapper.LinkMapper;
import com.harvey.link.service.LinkService;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@RestController
public class LinkController {
    @Resource
    private LinkService linkService;
    
    @Resource
    private LinkMapper linkMapper;
    
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
    
    @PostMapping("/api/shortic/link/v1/page")
    public Result<LinkPageVo> pageLink(@RequestBody LinkPageDto linkPageDto) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        String gid = linkPageDto.getGid();
        Long pageNo = linkPageDto.getPageNo();
        Long pageSize = linkPageDto.getPageSize();
        
        Page<LinkDo> page = new Page<>(pageNo, pageSize);
        
        linkService.lambdaQuery()
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
        
        return Result.success(linkPageVo);
    }
    
    @PostMapping("/api/shortic/link/v1/count")
    public Result countLink(@RequestBody List<String> gidList) {
        List<Map<String, Object>> linkDoMapList = linkMapper.countLink(gidList);
        
        List<LinkGroupCountVo> linkGroupCountVoList = linkDoMapList.stream()
            .map(linkDoMap -> {
                LinkGroupCountVo linkGroupCountVo = new LinkGroupCountVo();
                linkGroupCountVo.setGid((String) linkDoMap.get("gid"));
                linkGroupCountVo.setCount((Long) linkDoMap.get("gid_cnt"));
                return linkGroupCountVo;
            })
            .toList();
        
        return Result.success(linkGroupCountVoList);
    }
}
