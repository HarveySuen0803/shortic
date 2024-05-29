package com.harvey.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.util.HashBase62Util;
import com.harvey.link.entitiy.domain.LinkDo;
import com.harvey.link.mapper.LinkMapper;
import com.harvey.link.service.LinkService;
import org.springframework.stereotype.Service;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDo> implements LinkService {
    @Override
    public String getShortUri(String longUrl) {
        return HashBase62Util.toBase62(longUrl, 6);
    }
}
