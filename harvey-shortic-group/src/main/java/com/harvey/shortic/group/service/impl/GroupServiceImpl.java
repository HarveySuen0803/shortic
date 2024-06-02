package com.harvey.shortic.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.support.RandomStringGenerator;
import com.harvey.shortic.group.common.entity.domain.GroupDo;
import com.harvey.shortic.group.mapper.GroupMapper;
import com.harvey.shortic.group.service.GroupService;
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
    public String genUniqueGid(Long userId) {
        String gid = genGid();
        
        while (isKeyExisted(gid, userId)) {
            gid = genGid();
        }
        
        return gid;
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
