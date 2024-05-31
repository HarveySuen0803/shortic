package com.harvey.link.constant;

import com.harvey.common.constant.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public class LinkResult extends Result {
    public static final Result GID_INVALID = new Result(3001, "gid is invalid");
    
    public static final Result SHORT_DIM_INVALID = new Result(3002, "shortDim is invalid");
    
    public static final Result SHORT_URI_INVALID = new Result(3003, "shortUri is invalid");
    
    public static final Result LONG_URL_INVALID = new Result(3004, "longUrl is invalid");
    
    public static final Result SHORT_URI_GENERATE_FAILURE = new Result(3005, "failed to generate short uri");
}