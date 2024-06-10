package com.harvey.shortic.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.shortic.group.common.model.po.GroupPo;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
public interface GroupService extends IService<GroupPo> {
    String genGid();
    
    String genUniqueGid(Long userId);
    
    boolean isKeyExisted(String gid, Long userId);
}
