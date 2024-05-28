package com.harvey.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.util.RandomStringGenerator;
import com.harvey.group.entity.domain.GroupDo;
import com.harvey.group.mapper.GroupMapper;
import com.harvey.group.service.GroupService;
import org.springframework.stereotype.Service;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDo> implements GroupService {
    @Override
    public String genGid() {
        return RandomStringGenerator.gen(6, true, true, false, false);
    }
    
    @Override
    public boolean isKeyExisted(String gid, Long userId) {
        boolean isExisted = lambdaQuery()
            .eq(GroupDo::getGid, gid)
            .eq(GroupDo::getUserId, userId)
            .exists();
        
        return isExisted;
    }
}
