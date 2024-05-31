package com.harvey.group.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.harvey.common.constant.Constant;
import com.harvey.common.constant.Result;
import com.harvey.common.exception.ClientException;
import com.harvey.group.common.UserContextHolder;
import com.harvey.group.entity.domain.GroupDo;
import com.harvey.group.entity.dto.GroupAddDto;
import com.harvey.group.entity.dto.GroupDeleteDto;
import com.harvey.group.entity.dto.GroupSortDto;
import com.harvey.group.entity.dto.GroupUpdateDto;
import com.harvey.group.entity.vo.GroupVo;
import com.harvey.group.result.GroupResult;
import com.harvey.group.service.GroupService;
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

        GroupDo groupDo = new GroupDo();
        groupDo.setName(name);
        groupDo.setGid(gid);
        groupDo.setUserId(userId);
        groupService.save(groupDo);
        
        return Result.success();
    }
    
    @PreAuthorize("hasAuthority('GROUP_R')")
    @GetMapping("/api/shortic/group/v1/list")
    public Result<List<GroupVo>> listGroup() {
        Long userId = UserContextHolder.getUserId();
        
        List<GroupDo> groupDoList = groupService.lambdaQuery()
            .eq(GroupDo::getUserId, userId)
            .eq(GroupDo::getIsDeleted, Constant.NOT_DELETED)
            .list();
        
        List<GroupVo> groupVoList = groupDoList.stream()
            .map(groupDo -> BeanUtil.copyProperties(groupDo, GroupVo.class))
            .toList();
        
        return Result.success(groupVoList);
    }
    
    @Transactional
    @PutMapping("/api/shortic/group/v1")
    public Result<Void> updateGroup(@RequestBody GroupUpdateDto groupUpdateDto) {
        Long userId = UserContextHolder.getUserId();
        String gid = groupUpdateDto.getGid();
        String name = groupUpdateDto.getName();
        
        GroupDo groupDo = groupService.lambdaQuery()
            .eq(GroupDo::getGid, gid)
            .eq(GroupDo::getUserId, userId)
            .eq(GroupDo::getIsDeleted, Constant.NOT_DELETED)
            .one();
        if (ObjUtil.isNull(groupDo)) {
            throw new ClientException(GroupResult.GROUP_NOT_FOUND);
        }
        
        groupDo.setName(name);
        
        groupService.saveOrUpdate(groupDo);
        
        return Result.success();
    }
    
    @Transactional
    @DeleteMapping("/api/shortic/group/v1")
    public Result<Void> deleteGroup(@RequestBody GroupDeleteDto groupDeleteDto) {
        String gid = groupDeleteDto.getGid();
        Long userId = UserContextHolder.getUserId();
        
        GroupDo groupDo = groupService.lambdaQuery()
            .eq(GroupDo::getGid, gid)
            .eq(GroupDo::getUserId, userId)
            .eq(GroupDo::getIsDeleted, Constant.NOT_DELETED)
            .one();
        if (ObjUtil.isNull(groupDo)) {
            throw new ClientException(GroupResult.GROUP_NOT_FOUND);
        }
        
        groupDo.setIsDeleted(Constant.DELETED);
        
        groupService.saveOrUpdate(groupDo);
        
        return Result.success();
    }
    
    @Transactional
    @PostMapping("/api/shortic/group/v1/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortDto> groupSortDtoList) {
        Long userId = UserContextHolder.getUserId();
        
        List<String> gidList = groupSortDtoList.stream()
            .map(GroupSortDto::getGid)
            .toList();
        
        List<GroupDo> groupDoList = groupService.lambdaQuery()
            .in(GroupDo::getGid, gidList)
            .eq(GroupDo::getUserId, userId)
            .eq(GroupDo::getIsDeleted, Constant.NOT_DELETED)
            .list();
        
        Map<String, Integer> gidToSortMap = groupSortDtoList.stream()
            .collect(Collectors.toMap(GroupSortDto::getGid, GroupSortDto::getSort));
        
        groupDoList = groupDoList.stream().peek(groupDo -> {
            String gid = groupDo.getGid();
            Integer sort = gidToSortMap.get(gid);
            groupDo.setSort(sort);
        }).toList();
        
        groupService.saveOrUpdateBatch(groupDoList);
        
        return Result.success();
    }
}
