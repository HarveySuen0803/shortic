package com.harvey.shortic.group.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.group.GroupResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Data
@NoArgsConstructor
public class GroupUpdateDto implements Serializable {
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
    
    @Serial
    private static final long serialVersionUID = 1L;
}
