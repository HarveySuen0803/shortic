package com.harvey.shortic.group.common.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Data
public class GroupVo implements Serializable {
    private String gid;
    
    private String name;
    
    private Long linkCount;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
