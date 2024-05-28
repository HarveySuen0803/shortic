package com.harvey.group.entity.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Data
@TableName("t_group")
public class GroupDo implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String gid;

    private String name;

    private Long userId;
    
    private Integer sort;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    private Integer isEnabled;

    @Serial
    private static final long serialVersionUID = 1L;
}