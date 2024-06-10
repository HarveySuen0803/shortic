package com.harvey.shortic.link.common.model.dto;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.constant.Constant;
import com.harvey.common.exception.ClientException;
import com.harvey.shortic.link.common.constant.LinkResult;
import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-30
 */
@Data
public class LinkPageDto {
    private String gid;
    
    private Long pageNo;
    
    private Long pageSize;
    
    public void setGid(String gid) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(LinkResult.GID_INVALID);
        }
        this.gid = gid;
    }
    
    public void setPageNo(Long pageNo) {
        if (pageNo == null || pageNo < 0) {
            pageNo = Constant.PAGE_NO;
        }
        this.pageNo = pageNo;
    }
    
    public void setPageSize(Long pageSize) {
        if (pageSize == null) {
            pageSize = Constant.PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }
}
