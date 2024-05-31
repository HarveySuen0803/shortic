package com.harvey.link.entitiy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-30
 */
@Data
public class LinkVo {
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
}