package com.harvey.link.entitiy.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Data
@TableName("t_link")
public class Link implements Serializable {
    private Long id;

    private String gid;

    private Long clickCount;

    private String shortDom;

    private String shortUri;

    private String shortUrl;

    private String longUrl;

    private String desc;

    private Integer expireType;

    private Date expireTime;

    private Long updateBy;

    private Date updateTime;

    private Long createBy;

    private Date createTime;

    private Integer isEnabled;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}