package com.harvey.user.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
@Data
@TableName("t_user_auth")
public class UserAuthDo {
    private Long id;
    
    private Long userId;
    
    private Long authId;
}
