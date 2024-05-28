package com.harvey.group.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.group.result.GroupResult;
import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Data
public class GroupUpdateDto {
    private String gid;
    
    private String name;
    
    public GroupUpdateDto(String gid, String name) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(GroupResult.GID_INVALID);
        }
        if (StrUtil.isBlank(name)) {
            throw new ClientException(GroupResult.NAME_INVALID);
        }
        
        this.gid = gid;
        this.name = name;
    }
}
