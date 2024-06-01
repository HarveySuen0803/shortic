package com.harvey.rpc.link.service;

import com.harvey.rpc.link.entity.rep.LinkGroupCountRep;
import com.harvey.rpc.link.entity.req.LinkGroupCountReq;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
public interface RpcLinkService {
    List<LinkGroupCountRep> countLink(List<String> gidList);
}
