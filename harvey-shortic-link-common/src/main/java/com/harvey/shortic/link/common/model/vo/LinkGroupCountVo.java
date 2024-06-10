package com.harvey.shortic.link.common.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
@Data
public class LinkGroupCountVo implements Serializable {
    private String gid;
    
    private long linkCount;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
