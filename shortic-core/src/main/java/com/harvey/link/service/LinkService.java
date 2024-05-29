package com.harvey.link.service;

import com.harvey.link.entitiy.domain.LinkDo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public interface LinkService extends IService<LinkDo> {
    String getShortUri(String longUrl);
}
