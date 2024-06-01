package com.harvey.rpc.link.service;

import com.harvey.rpc.link.entity.dto.LinkGroupCountDto;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
public interface RpcLinkService {
    List<LinkGroupCountDto> countLink(List<String> gidList);
}
