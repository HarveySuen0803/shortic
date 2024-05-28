package com.harvey.group.result;

import com.harvey.common.result.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
public class GroupResult extends Result {
    public static final Result NAME_INVALID = new Result(2001, "name is invalid");
    
    public static final Result GID_INVALID = new Result(2002, "gid is invalid");
}
