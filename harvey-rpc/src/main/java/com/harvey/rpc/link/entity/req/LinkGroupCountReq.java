package com.harvey.rpc.link.entity.req;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.link.LinkResult;
import com.harvey.common.result.Result;
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
public class LinkGroupCountReq implements Serializable {
    private String gid;
    
    private Long count;
    
    public LinkGroupCountReq(String gid, Long count) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(LinkResult.GID_INVALID);
        }
        if (ObjUtil.isNull(count)) {
            throw new ClientException(Result.PARAM_INVALID);
        }
        
        this.gid = gid;
        this.count = count;
    }
    
    @Serial
    private static final long serialVersionUID = 1L;
}
