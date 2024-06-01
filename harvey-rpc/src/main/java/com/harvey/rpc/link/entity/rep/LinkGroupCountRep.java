package com.harvey.rpc.link.entity.rep;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-31
 */
@Data
public class LinkGroupCountRep implements Serializable {
    private String gid;
    
    private Long count;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
