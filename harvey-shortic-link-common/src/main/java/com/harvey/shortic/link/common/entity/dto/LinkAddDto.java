package com.harvey.shortic.link.common.entity.dto;

import cn.hutool.core.util.StrUtil;
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
 * @Date 2024-05-29
 */
@Data
@NoArgsConstructor
public class LinkAddDto implements Serializable {
    private String gid;
    
    private String shortDom;
    
    private String longUrl;
    
    private String description;
    
    private Byte expireType;
    
    private LocalDateTime expireTime;
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    public void setGid(String gid) {
        if (StrUtil.isBlank(gid)) {
            throw new ClientException(LinkResult.GID_INVALID);
        }
        this.gid = gid;
    }
    
    public void setShortDom(String shortDom) {
        if (StrUtil.isBlank(shortDom)) {
            throw new ClientException(LinkResult.SHORT_DOM_INVALID);
        }
        this.shortDom = shortDom;
    }
    
    public void setLongUrl(String longUrl) {
        if (StrUtil.isBlank(longUrl)) {
            throw new ClientException(LinkResult.LONG_URL_INVALID);
        }
        this.longUrl = longUrl;
    }
}
