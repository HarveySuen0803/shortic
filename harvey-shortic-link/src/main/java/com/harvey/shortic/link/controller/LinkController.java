package com.harvey.shortic.link.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.constant.Constant;
import com.harvey.common.result.Result;
import com.harvey.rpc.link.entity.rep.LinkGroupCountRep;
import com.harvey.shortic.link.entitiy.domain.LinkDo;
import com.harvey.shortic.link.entitiy.dto.LinkAddDto;
import com.harvey.shortic.link.entitiy.dto.LinkPageDto;
import com.harvey.shortic.link.entitiy.vo.LinkPageVo;
import com.harvey.shortic.link.entitiy.vo.LinkVo;
import com.harvey.shortic.link.service.LinkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    
    @PostMapping("/api/shortic/link/v1/page")
    public Result<LinkPageVo> pageLink(@RequestBody LinkPageDto linkPageDto) {
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
    public Result<List<LinkGroupCountRep>> countLink(@RequestBody List<String> gidList) {
        List<LinkGroupCountRep> linkGroupCountRepList = linkService.countLink(gidList);
        
        return Result.success(linkGroupCountRepList);
    }
}
