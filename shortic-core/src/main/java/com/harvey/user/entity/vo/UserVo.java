package com.harvey.user.entity.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-22
 */
@Data
public class UserVo {
    private String username;
    
    private String email;
    
    public void mask() {
        maskEmail();
    }
    
    public void maskEmail() {
        String[] splits = email.split("@");
        if (splits.length != 2) {
            return;
        }
        
        String s0 = splits[0];
        String s1 = splits[1];
        if (StrUtil.isBlank(s0) || StrUtil.isBlank(s1)) {
            return;
        }
        
        email = s0.charAt(0) + "*****" + "@" + s1;
    }
}