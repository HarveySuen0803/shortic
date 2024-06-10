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
public class GroupAddDto implements Serializable {
    private String name;
    
    public GroupAddDto(String name) {
        if (StrUtil.isBlank(name)) {
            throw new ClientException(GroupResult.NAME_INVALID);
        }
        
        this.name = name;
    }
    
    @Serial
    private static final long serialVersionUID = 1L;
}
