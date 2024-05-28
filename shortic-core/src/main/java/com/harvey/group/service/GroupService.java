package com.harvey.group.service;

import com.harvey.group.entity.domain.GroupDo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
public interface GroupService extends IService<GroupDo> {
    String genGid();
    
    boolean isKeyExisted(String gid, Long userId);
}
