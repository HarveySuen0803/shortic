package com.harvey.user.common.model.po;

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
@TableName(value ="t_role_auth")
public class RoleAuthPo implements Serializable {
    private Long id;
    
    private Long roleId;
    
    private Long authId;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
