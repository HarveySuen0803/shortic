package com.harvey.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.link.entitiy.domain.Link;
import com.harvey.link.service.LinkService;
import com.harvey.link.mapper.LinkMapper;
import org.springframework.stereotype.Service;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
}
