package com.harvey.user.common.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
@Data
@TableName(value ="t_role")
public class RolePo implements Serializable {
    private Long id;

    private String name;

    @Serial
    private static final long serialVersionUID = 1L;
}