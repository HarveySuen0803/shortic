package com.harvey.group.result;

import com.harvey.common.constant.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
public class GroupResult extends Result {
    public static final Result GROUP_NOT_FOUND = new Result(2001, "group not found");
    
    public static final Result NAME_INVALID = new Result(2002, "name is invalid");
    
    public static final Result GID_INVALID = new Result(2003, "gid is invalid");
}
