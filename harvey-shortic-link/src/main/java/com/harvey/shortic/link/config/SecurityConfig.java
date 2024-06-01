package com.harvey.shortic.link.config;

import com.harvey.shortic.link.filter.LoginTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-30
 */
@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity,
        LoginTokenFilter loginTokenFilter
    ) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        
        httpSecurity.authorizeHttpRequests((authorize) -> {
            authorize.anyRequest().authenticated();
        });
        
        httpSecurity.addFilterBefore(loginTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }
}
