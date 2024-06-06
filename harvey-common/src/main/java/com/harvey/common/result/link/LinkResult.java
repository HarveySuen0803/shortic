package com.harvey.common.result.link;

import com.harvey.common.result.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public class LinkResult extends Result {
    public static final Result GID_INVALID = new Result(3101, "gid is invalid");
    
    public static final Result SHORT_DOM_INVALID = new Result(3102, "shortDom is invalid");
    
    public static final Result SHORT_URI_INVALID = new Result(3103, "shortUri is invalid");
    
    public static final Result SHORT_URL_INVALID = new Result(3104, "shortUrl is invalid");
    
    public static final Result LONG_URL_INVALID = new Result(3105, "longUrl is invalid");
    
    public static final Result SRC_GID_INVALID = new Result(3105, "srcGid is invalid");
    
    public static final Result TAR_GID_INVALID = new Result(3106, "tarGid is invalid");
    
    public static final Result EXPIRE_TYPE_INVALID = new Result(3107, "expire type is invalid");
    
    public static final Result EXPIRE_TYPE_TIME = new Result(3108, "expire time is invalid");
    
    public static final Result SHORT_URI_GENERATE_FAILURE = new Result(3201, "failed to generate short uri");
    
    public static final Result SHORT_URI_NOT_EXISTS = new Result(3202, "short uri not exists");
    
    public static final Result LINK_NOT_EXISTS = new Result(3202, "short link not exists");
}
