package com.harvey.shortic.group.common.model.dto;

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
 * @Date 2024-05-28
 */
@Data
@NoArgsConstructor
public class GroupDeleteDto implements Serializable {
    private String gid;
    
    public GroupDeleteDto(String gid) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(GroupResult.GID_INVALID);
        }
        
        this.gid = gid;
    }
    
    @Serial
    private static final long serialVersionUID = 1L;
}
