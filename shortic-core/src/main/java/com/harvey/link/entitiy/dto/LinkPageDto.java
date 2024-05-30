package com.harvey.link.entitiy.dto;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.constant.Constant;
import com.harvey.common.exception.ClientException;
import com.harvey.link.constant.LinkResult;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-30
 */
@Data
@NoArgsConstructor
public class LinkPageDto {
    private String gid;
    
    private Long pageNo;
    
    private Long pageSize;
    
    public LinkPageDto(String gid, Long pageNo, Long pageSize) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(LinkResult.GID_INVALID);
        }
        if (pageNo == null || pageNo < 0) {
            pageNo = Constant.PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = Constant.PAGE_SIZE;
        }
        
        this.gid = gid;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
