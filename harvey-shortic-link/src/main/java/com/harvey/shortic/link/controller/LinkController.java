package com.harvey.shortic.link.controller;

import com.harvey.common.result.Result;
import com.harvey.shortic.link.common.entity.dto.LinkPageDto;
import com.harvey.shortic.link.common.entity.vo.LinkPageVo;
import com.harvey.shortic.link.common.entity.dto.LinkAddDto;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
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
        linkService.addLink(linkAddDto);
        
        return Result.success();
    }
    
    @PostMapping("/api/shortic/link/v1/page")
    public Result<LinkPageVo> pageLink(@RequestBody LinkPageDto linkPageDto) {
        LinkPageVo linkPageVo = linkService.pageLink(linkPageDto);
        
        return Result.success(linkPageVo);
    }
    
    @PostMapping("/api/shortic/link/v1/count")
    public Result<List<LinkGroupCountVo>> countLink(@RequestBody List<String> gidList) {
        List<LinkGroupCountVo> linkGroupCountDtoList = linkService.countLink(gidList);
        
        return Result.success(linkGroupCountDtoList);
    }
}
