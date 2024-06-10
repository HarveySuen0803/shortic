package com.harvey.user.support;

import com.harvey.common.constant.Result;
import com.harvey.common.support.ResponseUtil;
import com.harvey.log.support.ExceptionLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-01
 */
@Component
public class GlobalAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * Triggers when an authenticated user attempts to access a resource they are not authorized for.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ExceptionLogger.error(accessDeniedException, request);
        ResponseUtil.write(response, Result.FORBIDDEN);
    }
}
