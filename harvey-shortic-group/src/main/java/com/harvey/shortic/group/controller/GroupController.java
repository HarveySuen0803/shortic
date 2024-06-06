package com.harvey.shortic.group.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.harvey.common.constant.Constant;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.Result;
import com.harvey.common.result.group.GroupResult;
import com.harvey.security.service.UserContextHolder;
import com.harvey.shortic.group.common.entity.dto.GroupAddDto;
import com.harvey.shortic.group.common.entity.dto.GroupDeleteDto;
import com.harvey.shortic.group.common.entity.dto.GroupSortDto;
import com.harvey.shortic.group.common.entity.dto.GroupUpdateDto;
import com.harvey.shortic.group.common.entity.po.GroupPo;
import com.harvey.shortic.group.common.entity.vo.GroupVo;
import com.harvey.shortic.group.service.GroupService;
import com.harvey.shortic.link.common.entity.vo.LinkGroupCountVo;
import com.harvey.shortic.link.rpc.service.LinkRpcService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
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
@PreAuthorize("hasAuthority('GROUP_R')")
@EnableMethodSecurity
public class GroupController {
    @Resource
    private UserContextHolder userContextHolder;
    
    @Resource
    private GroupService groupService;
    
    @DubboReference(url = "dubbo://127.0.0.1:30115", parameters = {"serialization", "fastjson2"})
    private LinkRpcService linkRpcService;
    
    @Transactional
    @PostMapping("/api/shortic/group/v1")
    public Result<Void> addGroup(@RequestBody GroupAddDto groupAddDto) {
        String name = groupAddDto.getName();
        
        Long userId = userContextHolder.getUserId();
        
        String gid = groupService.genUniqueGid(userId);

        GroupPo groupPo = new GroupPo();
        groupPo.setName(name);
        groupPo.setGid(gid);
        groupPo.setUserId(userId);
        groupService.save(groupPo);
        
        return Result.success();
    }
    
    @GetMapping("/api/shortic/group/v1/list")
    public Result<List<GroupVo>> listGroup() {
        Long userId = userContextHolder.getUserId();
        
        List<GroupPo> groupPoList = groupService.lambdaQuery()
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getDeletedFlag, Constant.NOT_DELETED)
            .list();
        
        List<String> gidList = groupPoList.stream().map(GroupPo::getGid).toList();
        
        List<LinkGroupCountVo> linkGroupCountVoList = linkRpcService.countLink(gidList);
        Map<String, Long> gidToLinkCountMap = linkGroupCountVoList.stream()
            .collect(
                Collectors.toMap(
                    LinkGroupCountVo::getGid,
                    LinkGroupCountVo::getLinkCount
                )
            );
        
        List<GroupVo> groupVoList = groupPoList.stream()
            .map(groupPo -> {
                String gid = groupPo.getGid();
                Long linkCount = gidToLinkCountMap.get(gid);
                if (linkCount == null) {
                    linkCount = 0L;
                }
                
                GroupVo groupVo = BeanUtil.copyProperties(groupPo, GroupVo.class);
                groupVo.setLinkCount(linkCount);
                
                return groupVo;
            })
            .toList();
        
        return Result.success(groupVoList);
    }
    
    @Transactional
    @PutMapping("/api/shortic/group/v1")
    public Result<Void> updateGroup(@RequestBody GroupUpdateDto groupUpdateDto) {
        Long userId = userContextHolder.getUserId();
        String gid = groupUpdateDto.getGid();
        String name = groupUpdateDto.getName();
        
        GroupPo groupPo = groupService.lambdaQuery()
            .eq(GroupPo::getGid, gid)
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getDeletedFlag, Constant.NOT_DELETED)
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
        Long userId = userContextHolder.getUserId();
        
        GroupPo groupPo = groupService.lambdaQuery()
            .eq(GroupPo::getGid, gid)
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getDeletedFlag, Constant.NOT_DELETED)
            .one();
        if (ObjUtil.isNull(groupPo)) {
            throw new ClientException(GroupResult.GROUP_NOT_FOUND);
        }
        
        groupPo.setDeletedFlag(Constant.DELETED);
        
        groupService.saveOrUpdate(groupPo);
        
        return Result.success();
    }
    
    @Transactional
    @PostMapping("/api/shortic/group/v1/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortDto> groupSortDtoList) {
        Long userId = userContextHolder.getUserId();
        
        List<String> gidList = groupSortDtoList.stream()
            .map(GroupSortDto::getGid)
            .toList();
        
        List<GroupPo> groupPoList = groupService.lambdaQuery()
            .in(GroupPo::getGid, gidList)
            .eq(GroupPo::getUserId, userId)
            .eq(GroupPo::getDeletedFlag, Constant.NOT_DELETED)
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
