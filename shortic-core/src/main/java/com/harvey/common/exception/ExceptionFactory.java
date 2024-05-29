package com.harvey.common.exception;

import cn.hutool.core.util.ObjUtil;
import com.harvey.common.constant.Result;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-28
 */
public class ExceptionFactory {
    public static <T> BaseException getException(ExceptionType exceptionType, Result<T> result) {
        if (ObjUtil.equal(ExceptionType.CLIENT, exceptionType)) {
            return new ClientException(result);
        }
        
        if (ObjUtil.equal(ExceptionType.SERVER, exceptionType)) {
            return new ServerException(result);
        }
        
        if (ObjUtil.equal(ExceptionType.REMOTE, exceptionType)) {
            return new RemoteException(result);
        }
        
        return new BaseException(result);
    }
}
