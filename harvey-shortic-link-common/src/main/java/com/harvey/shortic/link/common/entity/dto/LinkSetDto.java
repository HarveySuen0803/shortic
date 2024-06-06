package com.harvey.shortic.link.common.entity.dto;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.harvey.common.exception.ClientException;
import com.harvey.common.result.link.LinkResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-06
 */
@Data
public class LinkSetDto implements Serializable {
    private String shortUri;
    
    private String longUrl;
    
    private String srcGid;
    
    private String tarGid;
    
    private String description;
    
    private Byte expireType;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    public void setShortUri(String shortUri) {
        if (StrUtil.isBlank(shortUri)) {
            throw new ClientException(LinkResult.SHORT_URI_INVALID);
        }
        this.shortUri = shortUri;
    }
    
    public void setLongUrl(String longUrl) {
        if (StrUtil.isBlank(longUrl)) {
            throw new ClientException(LinkResult.LONG_URL_INVALID);
        }
        this.longUrl = longUrl;
    }
    
    public void setSrcGid(String srcGid) {
        if (StrUtil.isBlank(srcGid)) {
            throw new ClientException(LinkResult.SRC_GID_INVALID);
        }
        this.srcGid = srcGid;
    }
    
    public void setTarGid(String tarGid) {
        if (StrUtil.isBlank(tarGid)) {
            throw new ClientException(LinkResult.TAR_GID_INVALID);
        }
        this.tarGid = tarGid;
    }
}
