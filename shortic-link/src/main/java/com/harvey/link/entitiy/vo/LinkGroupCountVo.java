package com.harvey.link.entitiy.vo;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.constant.Result;
import com.harvey.common.exception.ClientException;
import com.harvey.common.exception.ServerException;
import com.harvey.link.constant.LinkResult;
import lombok.AllArgsConstructor;
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
