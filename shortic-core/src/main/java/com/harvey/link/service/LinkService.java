package com.harvey.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.link.entitiy.domain.LinkDo;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public interface LinkService extends IService<LinkDo> {
    String getShortUri(String longUrl);
    
    boolean isShortUriExists(String shortUri);
    
    boolean isShortUriNotExists(String shortUri);
}
