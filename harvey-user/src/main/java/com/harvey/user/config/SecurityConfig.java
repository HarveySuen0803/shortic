package com.harvey.user.config;

import com.harvey.security.common.service.UserContextHolder;
import com.harvey.security.service.impl.UserContextHolderImpl;
import com.harvey.security.support.AuthenticationFilter;
import com.harvey.security.common.constant.SecurityHttpUri;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity,
        AuthenticationSuccessHandler authenticationSuccessHandler,
        AuthenticationFailureHandler authenticationFailureHandler,
        LogoutSuccessHandler logoutSuccessHandler,
        AuthenticationFilter authenticationFilter,
        AuthenticationEntryPoint authenticationEntryPoint,
        AuthenticationManager authenticationManager,
        AccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        
        httpSecurity.authorizeHttpRequests((authorize) -> {
            authorize.requestMatchers(SecurityHttpUri.LOGIN, SecurityHttpUri.LOGOUT, SecurityHttpUri.REGISTER, SecurityHttpUri.REFRESH).permitAll()
                .anyRequest().authenticated();
        });
        
        httpSecurity.formLogin((formLogin) -> {
            formLogin.loginProcessingUrl(SecurityHttpUri.LOGIN)
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll();
        });
        
        httpSecurity.logout((logout) -> {
            logout.logoutUrl(SecurityHttpUri.LOGOUT)
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
        });
        
        httpSecurity.authenticationManager(authenticationManager);
        
        httpSecurity.exceptionHandling((exception) -> {
            exception.authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        });
        
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
    
    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }
    
    @Bean
    public UserContextHolder userContextHolder() {
        return new UserContextHolderImpl();
    }
}
