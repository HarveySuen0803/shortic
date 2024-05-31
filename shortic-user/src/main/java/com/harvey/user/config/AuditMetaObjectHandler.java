package com.harvey.user.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.harvey.user.common.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-27
 */
@Component
public class AuditMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = UserContextHolder.getUserId();
        
        strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        strictInsertFill(metaObject, "createBy", Long.class, userId);
        strictInsertFill(metaObject, "updateBy", Long.class, userId);
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = UserContextHolder.getUserId();
        
        strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        strictUpdateFill(metaObject, "updateBy", Long.class, userId);
    }
}
