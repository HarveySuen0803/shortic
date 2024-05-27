package com.harvey.user.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Data
@TableName("t_user")
public class UserDo implements Serializable, UserDetails {
    private Long id;

    private String username;

    private String password;

    private String email;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private Long createBy;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    private Long updateBy;

    private Integer isAccountNoExpired;
    
    private Integer isCredentialsNoExpired;
    
    private Integer isAccountNoLocked;
    
    private Integer isEnabled;
    
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