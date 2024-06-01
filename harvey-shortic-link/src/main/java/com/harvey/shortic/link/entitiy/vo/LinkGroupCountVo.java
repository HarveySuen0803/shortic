package com.harvey.shortic.link.entitiy.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
@Data
@NoArgsConstructor
public class LinkGroupCountVo implements Serializable {
    private String gid;
    
    private Long count;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
