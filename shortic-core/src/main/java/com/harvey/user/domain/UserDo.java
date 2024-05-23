package com.harvey.user.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Data
@TableName("t_user")
public class UserDo implements Serializable {
    private Long id;

    private String name;

    private String password;

    private String phone;

    private String email;

    private Long deactiveTime;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}