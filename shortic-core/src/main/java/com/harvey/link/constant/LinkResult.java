package com.harvey.link.constant;

import com.harvey.common.constant.Result;
import lombok.Data;

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
}
