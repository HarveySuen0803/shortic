package com.harvey.shortic.group.common.model.dto;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.shortic.group.common.constant.GroupResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Data
@NoArgsConstructor
public class GroupSortDto implements Serializable {
    private String gid;
    
    private Integer sort;
    
    public GroupSortDto(String gid, Integer sort) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(GroupResult.GID_INVALID);
        }
        if (ObjUtil.isNotNull(sort)) {
            throw new ClientException(GroupResult.NAME_INVALID);
        }
        
        this.gid = gid;
        this.sort = sort;
    }
    
    @Serial
    private static final long serialVersionUID = 1L;
}
