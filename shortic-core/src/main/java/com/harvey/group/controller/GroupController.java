package com.harvey.group.controller;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.Result;
import com.harvey.group.entity.domain.GroupDo;
import com.harvey.group.result.GroupResult;
import com.harvey.group.service.GroupService;
import com.harvey.user.holder.UserContextHolder;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@RestController
public class GroupController {
    @Resource
    private GroupService groupService;
    
    @Transactional
    @PostMapping("/api/group/v1")
    public Result<Void> addGroup(String name) {
        if (StrUtil.isBlank(name)) {
            throw new ClientException(GroupResult.NAME_INVALID);
        }
        
        Long userId = UserContextHolder.getUserId();
        String gid = groupService.genGid();
        while (groupService.isKeyExisted(gid, userId)) {
            gid = groupService.genGid();
        }

        GroupDo groupDo = new GroupDo();
        groupDo.setName(name);
        groupDo.setGid(gid);
        groupDo.setUserId(userId);
        groupService.save(groupDo);
        
        return Result.success();
    }
}
