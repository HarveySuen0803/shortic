package com.harvey.user.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Data
@TableName("t_user")
public class UserDo implements Serializable, UserDetails {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private String email;

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

    private Integer isAccountNoExpired;
    
    private Integer isCredentialsNoExpired;
    
    private Integer isAccountNoLocked;
    
    private Byte isEnabled;
    
    private Byte isDeleted;
    
    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities;

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return isAccountNoExpired == 1;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isAccountNoLocked == 1;
    }
    
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return isCredentialsNoExpired == 1;
    }
    
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isEnabled == 1;
    }
}