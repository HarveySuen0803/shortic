package com.harvey.user.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
@Data
@TableName("t_user_role")
public class UserRoleDo implements Serializable {
    private Long id;
    
    private Long userId;
    
    private Long roleId;
    
    @Serial
    private static final long serialVersionUID = 1L;
}