package com.harvey.user.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName auth
 */
@Data
@TableName(value ="t_auth")
public class AuthDo implements Serializable {
    private Long id;

    private String name;

    @Serial
    private static final long serialVersionUID = 1L;
}