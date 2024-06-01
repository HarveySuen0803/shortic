package com.harvey.shortic.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.shortic.group.entity.domain.GroupDo;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
public interface GroupService extends IService<GroupDo> {
    String genGid();
    
    String genUniqueGid(Long userId);
    
    boolean isKeyExisted(String gid, Long userId);
}
