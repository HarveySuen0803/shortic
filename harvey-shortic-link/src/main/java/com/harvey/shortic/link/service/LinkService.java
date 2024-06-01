package com.harvey.shortic.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.rpc.link.entity.dto.LinkGroupCountDto;
import com.harvey.shortic.link.entitiy.domain.LinkDo;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public interface LinkService extends IService<LinkDo> {
    String getShortUri(String longUrl);
    
    boolean isShortUriExists(String shortUri);
    
    boolean isShortUriNotExists(String shortUri);
    
    List<LinkGroupCountDto> countLink(List<String> gidList);
}
