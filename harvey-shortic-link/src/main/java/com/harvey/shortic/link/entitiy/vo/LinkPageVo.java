package com.harvey.shortic.link.entitiy.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-30
 */
@Data
@NoArgsConstructor
public class LinkPageVo implements Serializable {
    List<LinkVo> linkVoList;
    
    Long totalSize;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
