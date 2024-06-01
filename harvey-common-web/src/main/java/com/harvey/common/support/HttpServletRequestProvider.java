package com.harvey.common.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-23
 */
public class HttpServletRequestProvider {
    public static HttpServletRequest get() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }
}