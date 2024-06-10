package com.harvey.user.support;

import com.harvey.common.constant.Result;
import com.harvey.common.support.ResponseUtil;
import com.harvey.log.support.ExceptionLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-01
 */
@Component
public class GlobalAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * Triggered when a user attempts to access a protected resource but has not yet authenticated.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ExceptionLogger.error(authException, request);
        ResponseUtil.write(response, Result.UNAUTHORIZED);
    }
}
