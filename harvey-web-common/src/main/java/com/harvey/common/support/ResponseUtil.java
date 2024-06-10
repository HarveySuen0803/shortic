package com.harvey.common.support;

import com.alibaba.fastjson2.JSON;
import com.harvey.common.constant.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
public class ResponseUtil {
    public static <T> void write(HttpServletResponse response, Result<T> result) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
