package com.harvey.user.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.user.common.UserResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Data
@NoArgsConstructor
public class UserDto implements Serializable {
    private String username;
    
    private String password;
    
    private String email;
    
    public UserDto(String username, String password, String email) {
        if (StrUtil.isBlank(username)) {
            throw new ClientException(UserResult.USERNAME_INVALID);
        }
        if (StrUtil.isBlank(password)) {
            throw new ClientException(UserResult.PASSWORD_INVALID);
        }
        if (StrUtil.isBlank(email)) {
            throw new ClientException(UserResult.EMAIL_INVALID);
        }
        
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    @Serial
    private static final long serialVersionUID = 1L;
}