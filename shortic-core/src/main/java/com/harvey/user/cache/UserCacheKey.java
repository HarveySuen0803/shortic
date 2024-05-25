package com.harvey.user.cache;

import com.harvey.common.cache.CacheKey;

import java.util.concurrent.TimeUnit;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
public class UserCacheKey extends CacheKey {
    public static final CacheKey LOGIN_TOKEN = new CacheKey("user:token:login:%s", 60 * 24 * 7L, TimeUnit.MINUTES);
    
    public static final CacheKey LIST = new CacheKey("user:list", 60 * 24 * 7L, TimeUnit.MINUTES);
    
    public static final CacheKey PAGE = new CacheKey("user:page:%s:%s", 60 * 24 * 7L, TimeUnit.MINUTES);
}