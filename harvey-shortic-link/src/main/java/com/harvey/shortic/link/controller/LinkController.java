package com.harvey.shortic.link.controller;

import com.harvey.common.result.Result;
import com.harvey.shortic.link.common.entity.dto.LinkAddDto;
import com.harvey.shortic.link.common.entity.dto.LinkPageDto;
import com.harvey.shortic.link.common.entity.dto.LinkSetDto;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
import com.harvey.shortic.link.common.entity.vo.LinkPageVo;
import com.harvey.shortic.link.service.LinkService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@RestController
@PreAuthorize("hasAuthority('LINK_R')")
@EnableMethodSecurity
public class LinkController {
    @Resource
    private LinkService linkService;
    
    @GetMapping("/api/shortic/link/v1/page")
    public Result<LinkPageVo> pageLink(@RequestParam LinkPageDto linkPageDto) {
        LinkPageVo linkPageVo = linkService.pageLink(linkPageDto);
        
        return Result.success(linkPageVo);
    }
    
    @GetMapping("/api/shortic/link/v1/count")
    public Result<List<LinkGroupCountVo>> countLink(@RequestParam List<String> gidList) {
        List<LinkGroupCountVo> linkGroupCountDtoList = linkService.countLink(gidList);
        
        return Result.success(linkGroupCountDtoList);
    }
    
    @PreAuthorize("hasAuthority('LINK_W')")
    @PutMapping("/api/shortic/link/v1")
    public Result<Void> addLink(@RequestBody LinkAddDto linkAddDto) {
        linkService.addLink(linkAddDto);
        
        return Result.success();
    }
    
    @PreAuthorize("hasAuthority('LINK_W')")
    @PostMapping("/api/shortic/link/v1")
    public Result<Void> setLink(@RequestBody LinkSetDto linkSetDto) {
        linkService.setLink(linkSetDto);
        
        return Result.success();
    }
}
