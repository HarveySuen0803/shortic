package com.harvey.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
public class CacheKey {
    public String prefix;
    
    public Long timeout;
    
    public TimeUnit unit;
    
    public CacheKey() {}
    
    public CacheKey(String prefix, Long timeout, TimeUnit unit) {
        this.prefix = prefix;
        this.timeout = timeout;
        this.unit = unit;
    }
    
    public String getKey(Object... param) {
        return String.format(prefix, param);
    }
}