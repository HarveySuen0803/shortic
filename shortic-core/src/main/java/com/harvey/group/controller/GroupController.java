package com.harvey.group.controller;

import cn.hutool.core.bean.BeanUtil;
import com.harvey.common.result.Result;
import com.harvey.group.entity.domain.GroupDo;
import com.harvey.group.entity.dto.GroupAddDto;
import com.harvey.group.entity.dto.GroupDeleteDto;
import com.harvey.group.entity.dto.GroupSortDto;
import com.harvey.group.entity.dto.GroupUpdateDto;
import com.harvey.group.entity.vo.GroupVo;
import com.harvey.group.service.GroupService;
import com.harvey.user.holder.UserContextHolder;
import jakarta.annotation.Resource;
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
public class GroupController {
    @Resource
    private GroupService groupService;
    
    @Transactional
    @PostMapping("/api/group/v1")
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
    
    @GetMapping("/api/group/v1/list")
    public Result<List<GroupVo>> listGroup() {
        Long userId = UserContextHolder.getUserId();
        
        List<GroupDo> groupDoList = groupService.lambdaQuery()
            .eq(GroupDo::getUserId, userId)
            .list();
        
        List<GroupVo> groupVoList = groupDoList.stream()
            .map(groupDo -> BeanUtil.copyProperties(groupDo, GroupVo.class))
            .toList();
        
        return Result.success(groupVoList);
    }
    
    @Transactional
    @PutMapping("/api/group/v1")
    public Result<Void> updateGroup(@RequestBody GroupUpdateDto groupUpdateDto) {
        Long userId = UserContextHolder.getUserId();
        String gid = groupUpdateDto.getGid();
        String name = groupUpdateDto.getName();
        
        groupService.lambdaUpdate()
            .set(GroupDo::getName, name)
            .eq(GroupDo::getGid, gid)
            .eq(GroupDo::getUserId, userId)
            .update();
        
        return Result.success();
    }
    
    @Transactional
    @DeleteMapping("/api/group/v1")
    public Result<Void> deleteGroup(@RequestBody GroupDeleteDto groupDeleteDto) {
        String gid = groupDeleteDto.getGid();
        Long userId = UserContextHolder.getUserId();
        
        groupService.lambdaUpdate()
            .set(GroupDo::getIsEnabled, 0)
            .eq(GroupDo::getGid, gid)
            .eq(GroupDo::getUserId, userId)
            .eq(GroupDo::getIsEnabled, 1)
            .update();
        
        return Result.success();
    }
    
    @Transactional
    @PostMapping("/api/group/v1/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortDto> groupSortDtoList) {
        Long userId = UserContextHolder.getUserId();
        
        List<String> gidList = groupSortDtoList.stream()
            .map(GroupSortDto::getGid)
            .toList();
        
        List<GroupDo> groupDoList = groupService.lambdaQuery()
            .in(GroupDo::getGid, gidList)
            .eq(GroupDo::getUserId, userId)
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
