package com.harvey.user.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.harvey.common.constant.Constant;
import com.harvey.common.exception.ClientException;
import com.harvey.common.util.ResponseUtil;
import com.harvey.common.constant.Result;
import com.harvey.log.common.ExceptionLog;
import com.harvey.user.constant.UserCacheKey;
import com.harvey.user.constant.UserConstant;
import com.harvey.user.entity.domain.UserDo;
import com.harvey.user.filter.LoginTokenFilter;
import com.harvey.user.constant.UserHttpUri;
import com.harvey.user.constant.UserResult;
import com.harvey.user.entity.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.Collection;
import java.util.HashMap;

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
    SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity,
        AuthenticationSuccessHandler authenticationSuccessHandler,
        AuthenticationFailureHandler authenticationFailureHandler,
        LogoutHandler logoutHandler,
        LogoutSuccessHandler logoutSuccessHandler,
        LoginTokenFilter loginTokenFilter,
        AuthenticationEntryPoint authenticationEntryPoint,
        AuthenticationManager authenticationManager,
        AccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        
        httpSecurity.authorizeHttpRequests((authorize) -> {
            authorize.requestMatchers(UserHttpUri.LOGIN, UserHttpUri.LOGOUT, UserHttpUri.REGISTER).permitAll()
                .anyRequest().authenticated();
        });
        
        httpSecurity.formLogin((formLogin) -> {
            formLogin.loginProcessingUrl(UserHttpUri.LOGIN)
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll();
        });
        
        httpSecurity.logout((logout) -> {
            logout.logoutUrl(UserHttpUri.LOGOUT)
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
        });
        
        httpSecurity.authenticationManager(authenticationManager);
        
        httpSecurity.exceptionHandling((exception) -> {
            exception.authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        });
        
        httpSecurity.addFilterBefore(loginTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(RedisTemplate redisTemplate) {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            UserDo userDo = (UserDo) authentication.getPrincipal();
            Long userId = userDo.getId();
            String username = userDo.getUsername();
            String password = userDo.getPassword();
            Collection<? extends GrantedAuthority> authorities = userDo.getAuthorities();
            String authoritiesJson = JSON.toJSONString(authorities);
            
            HashMap<String, Object> jwtPayload = new HashMap<>();
            jwtPayload.put(JWTPayload.ISSUED_AT, DateTime.now());
            jwtPayload.put(JWTPayload.EXPIRES_AT, DateTime.now().offset(UserConstant.LOGIN_TOKEN_TIMEOUT_UNIT, UserConstant.LOGIN_TOKEN_TIMEOUT));
            jwtPayload.put(JWTPayload.NOT_BEFORE, DateTime.now());
            jwtPayload.put(Constant.USER_ID, userId);
            jwtPayload.put(Constant.USERNAME, username);
            jwtPayload.put(Constant.PASSWORD, password);
            jwtPayload.put(Constant.AUTHORITIES, authoritiesJson);
            String loginToken = JWTUtil.createToken(jwtPayload, Constant.LOGIN_TOKEN_KEY);
            
            redisTemplate.opsForValue().set(
                UserCacheKey.LOGIN_TOKEN.getKey(userId),
                loginToken,
                UserCacheKey.LOGIN_TOKEN.timeout,
                UserCacheKey.LOGIN_TOKEN.unit
            );
            
            LoginVo loginVo = new LoginVo();
            loginVo.setUserId(userId);
            loginVo.setUsername(username);
            loginVo.setToken(loginToken);
            
            Result<LoginVo> loginVoResult = Result.success(loginVo);
            
            ResponseUtil.write(response, loginVoResult);
        };
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(HttpServletRequest req) {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {
            ExceptionLog.error(exception, req);
            ResponseUtil.write(response, Result.FAILURE);
        };
    }
    
    @Bean
    public LogoutHandler logoutHandler(RedisTemplate redisTemplate) {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // If the request header does not carry Login Token.
            String loginToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StrUtil.isBlank(loginToken)) {
                ResponseUtil.write(response, Result.UNAUTHORIZED);
                return;
            }
            
            // If the token is invalid or expired.
            JWT loginTokenJwt = JWT.of(loginToken).setKey(Constant.LOGIN_TOKEN_KEY);
            if (!loginTokenJwt.verify() || !loginTokenJwt.validate(0)) {
                ResponseUtil.write(response, Result.UNAUTHORIZED);
                return;
            }
            
            // Get user info from token.
            Long userId = Long.valueOf(loginTokenJwt.getPayload(Constant.USER_ID).toString());
            if (ObjUtil.isNull(userId)) {
                ResponseUtil.write(response, Result.UNAUTHORIZED);
                return;
            }
            
            redisTemplate.delete(UserCacheKey.LOGIN_TOKEN.getKey(userId));
        };
    }
    
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            ResponseUtil.write(response, Result.success());
        };
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(HttpServletRequest req) {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
            ExceptionLog.error(authException, req);
            ResponseUtil.write(response, Result.forbidden());
        };
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler(HttpServletRequest req) {
        return (HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) -> {
            ExceptionLog.error(accessDeniedException, req);
            ResponseUtil.write(response, Result.forbidden());
        };
    }
    
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
