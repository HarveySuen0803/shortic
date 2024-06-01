package com.harvey.shortic.group.config;

import com.harvey.common.support.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-01
 */
@Configuration
public class ExceptionConfig {
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
