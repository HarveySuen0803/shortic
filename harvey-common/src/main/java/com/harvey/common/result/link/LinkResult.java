package com.harvey.common.result.link;

import com.harvey.common.result.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
public class LinkResult extends Result {
    public static final Result GID_INVALID = new Result(3101, "gid is invalid");
    
    public static final Result SHORT_DIM_INVALID = new Result(3102, "shortDim is invalid");
    
    public static final Result SHORT_URI_INVALID = new Result(3103, "shortUri is invalid");
    
    public static final Result LONG_URL_INVALID = new Result(3104, "longUrl is invalid");
    
    public static final Result SHORT_URI_GENERATE_FAILURE = new Result(3201, "failed to generate short uri");
}
