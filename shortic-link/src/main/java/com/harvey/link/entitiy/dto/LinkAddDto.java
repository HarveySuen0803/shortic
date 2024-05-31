package com.harvey.link.entitiy.dto;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.link.constant.LinkResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Data
@NoArgsConstructor
public class LinkAddDto implements Serializable {
    private String gid;
    
    private String shortDim;
    
    private String longUrl;
    
    private String description;
    
    private Byte expireType;
    
    private LocalDateTime expireTime;
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    public LinkAddDto(String gid, String shortDim, String longUrl, String description, Byte expireType, LocalDateTime expireTime) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(LinkResult.GID_INVALID);
        }
        if (StrUtil.isBlank(shortDim)) {
            throw new ClientException(LinkResult.SHORT_DIM_INVALID);
        }
        if (StrUtil.isBlank(longUrl)) {
            throw new ClientException(LinkResult.LONG_URL_INVALID);
        }
        
        this.gid = gid;
        this.shortDim = shortDim;
        this.longUrl = longUrl;
        this.description = description;
        this.expireType = expireType;
        this.expireTime = expireTime;
    }
}
