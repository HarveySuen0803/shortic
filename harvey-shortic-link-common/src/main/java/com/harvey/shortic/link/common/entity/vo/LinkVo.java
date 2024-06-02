package com.harvey.shortic.link.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-30
 */
@Data
public class LinkVo implements Serializable {
    private String gid;
    
    private Long clickCount;
    
    private String shortDim;
    
    private String shortUri;
    
    private String shortUrl;
    
    private String longUrl;
    
    private String description;
    
    private Byte expireType;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
