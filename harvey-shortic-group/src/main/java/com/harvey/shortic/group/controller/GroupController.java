package com.harvey.shortic.group.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.harvey.common.constant.Constant;
import com.harvey.common.result.Result;
import com.harvey.common.exception.ClientException;
import com.harvey.security.support.UserContextHolder;
import com.harvey.shortic.group.common.entity.po.GroupPo;
import com.harvey.shortic.group.common.entity.dto.GroupAddDto;
import com.harvey.shortic.group.common.entity.dto.GroupDeleteDto;
import com.harvey.shortic.group.common.entity.dto.GroupSortDto;
import com.harvey.shortic.group.common.entity.dto.GroupUpdateDto;
import com.harvey.shortic.group.common.entity.vo.GroupVo;
import com.harvey.common.result.group.GroupResult;
import com.harvey.shortic.group.service.GroupService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@RestController
@EnableMethodSecurity
public class GroupController {
    @Resource
    private GroupService groupService;
    
    @Transactional
    @PostMapping("/api/shortic/group/v1")
    public Result<Void> addGroup(@RequestBody GroupAddDto groupAddDto) {
        String name = groupAddDto.getName();
        
        Long userId = UserContextHolder.getUserId();
        
        String gid = groupService.genUniqueGid(userId);

        GroupPo groupPo = new GroupPo();
        groupPo.setName(name);
        groupPo.setGid(gid);
        groupPo.setUserId(userId);
        groupService.save(groupPo);
        
        return Result.success();
    }
    
    @PreAuthorize("hasAuthority('GROUP_R')")
    @GetMapping("/api/shortic/group/v1/list")
    public Result<List<GroupVo>> listGroup() {
        Long userId = UserContextHolder.getUserId();
        
        List<GroupPo> groupPoList = groupService.lambdaQuery()
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getIsDeleted, Constant.NOT_DELETED)
            .list();
        
        List<GroupVo> groupVoList = groupPoList.stream()
            .map(groupPo -> BeanUtil.copyProperties(groupPo, GroupVo.class))
            .toList();
        
        return Result.success(groupVoList);
    }
    
    @Transactional
    @PutMapping("/api/shortic/group/v1")
    public Result<Void> updateGroup(@RequestBody GroupUpdateDto groupUpdateDto) {
        Long userId = UserContextHolder.getUserId();
        String gid = groupUpdateDto.getGid();
        String name = groupUpdateDto.getName();
        
        GroupPo groupPo = groupService.lambdaQuery()
            .eq(GroupPo::getGid, gid)
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getIsDeleted, Constant.NOT_DELETED)
            .one();
        if (ObjUtil.isNull(groupPo)) {
            throw new ClientException(GroupResult.GROUP_NOT_FOUND);
        }
        
        groupPo.setName(name);
        
        groupService.saveOrUpdate(groupPo);
        
        return Result.success();
    }
    
    @Transactional
    @DeleteMapping("/api/shortic/group/v1")
    public Result<Void> deleteGroup(@RequestBody GroupDeleteDto groupDeleteDto) {
        String gid = groupDeleteDto.getGid();
        Long userId = UserContextHolder.getUserId();
        
        GroupPo groupPo = groupService.lambdaQuery()
            .eq(GroupPo::getGid, gid)
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getIsDeleted, Constant.NOT_DELETED)
            .one();
        if (ObjUtil.isNull(groupPo)) {
            throw new ClientException(GroupResult.GROUP_NOT_FOUND);
        }
        
        groupPo.setIsDeleted(Constant.DELETED);
        
        groupService.saveOrUpdate(groupPo);
        
        return Result.success();
    }
    
    @Transactional
    @PostMapping("/api/shortic/group/v1/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortDto> groupSortDtoList) {
        Long userId = UserContextHolder.getUserId();
        
        List<String> gidList = groupSortDtoList.stream()
            .map(GroupSortDto::getGid)
            .toList();
        
        List<GroupPo> groupPoList = groupService.lambdaQuery()
            .in(GroupPo::getGid, gidList)
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getIsDeleted, Constant.NOT_DELETED)
            .list();
        
        Map<String, Integer> gidToSortMap = groupSortDtoList.stream()
            .collect(Collectors.toMap(GroupSortDto::getGid, GroupSortDto::getSort));
        
        groupPoList = groupPoList.stream().peek(groupPo -> {
            String gid = groupPo.getGid();
            Integer sort = gidToSortMap.get(gid);
            groupPo.setSort(sort);
        }).toList();
        
        groupService.saveOrUpdateBatch(groupPoList);
        
        return Result.success();
    }
}
