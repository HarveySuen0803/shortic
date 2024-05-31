package com.harvey.log.aspect;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.harvey.common.constant.Result;
import com.harvey.common.exception.ServerException;
import com.harvey.common.util.HttpServletRequestProvider;
import com.harvey.log.constant.ApiLogConstant;
import com.harvey.log.anno.ApiLog;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-23
 */
@Slf4j
@Aspect
@Component
public class ApiLogAspect {
    @Around("execution(* com.harvey..controller..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest req = HttpServletRequestProvider.get();
        if (ObjUtil.isNull(req)) {
            throw new ServerException(Result.INTERNAL_SERVER_ERROR);
        }
        
        String reqUri = req.getRequestURI();
        String reqMethod = req.getMethod();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String clazzName = signature.getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        String message = ApiLogConstant.DEFAULT_MESSAGE;
        
        ApiLog apiLog = signature.getMethod().getAnnotation(ApiLog.class);
        if (apiLog != null) {
            message = apiLog.message();
        }
        
        if (StrUtil.isNotBlank(message)) {
            log.info("{} {}, {}#{}, {}, {}.", reqMethod, reqUri, clazzName, methodName, args, message);
        } else {
            log.info("{} {}, {}#{}, {}.", reqMethod, reqUri, clazzName, methodName, args);
        }
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        long costTimeMillis = stopWatch.getLastTaskTimeMillis();
        
        log.info("{} {}, {}#{}, {}, cost {}ms.", reqMethod, reqUri, clazzName, methodName, args, costTimeMillis);
        
        return result;
    }
}
