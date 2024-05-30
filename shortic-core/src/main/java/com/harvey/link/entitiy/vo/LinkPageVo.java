package com.harvey.link.entitiy.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-30
 */
@Data
@NoArgsConstructor
public class LinkPageVo {
    List<LinkVo> linkVoList;
    
    Long totalSize;
}
